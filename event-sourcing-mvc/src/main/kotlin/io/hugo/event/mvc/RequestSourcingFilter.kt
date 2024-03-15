package io.hugo.event.mvc

import io.hugo.common.mvc.HttpServletRequestUtils.readHeaders
import io.hugo.event.blocking.dal.CommandRepo
import io.hugo.event.model.EventSourcingContext
import io.hugo.event.model.http.HttpCommandEntity
import io.hugo.event.model.http.HttpRequestCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

open class RequestSourcingFilter : OncePerRequestFilter() {
    @Autowired
    private lateinit var commandRepo: CommandRepo

    var exclusivePaths = listOf("/operation")

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        check(EventSourcingContext.getCurrentCommand() == null) {
            "The current thread is already processing a command ${EventSourcingContext.getCurrentCommand()}"
        }

        if (exclusivePaths.any { request.requestURI.startsWith(it) }) {
            return filterChain.doFilter(request, response)
        }
        val entity = buildCommandEntity(request)
        EventSourcingContext.setCurrentCommand(entity.commandData)
        val cachedRequest = ContentCachingRequestWrapper(request)
        // Cannot read body here otherwise other Filter won't be able to read it.
        // Introducing a buffered input stream would incur performance penalty by at least 50% in terms of message usage.

        try {
            filterChain.doFilter(cachedRequest, response)
        } finally {
            EventSourcingContext.setCurrentCommand(null)
            // contentAsByteArray only have value when the filterChain executed.
            entity.setBody(String(cachedRequest.contentAsByteArray))
            try {
                commandRepo.saveAndFlush(entity)
            } catch (ex: Exception) {
                logger.error("Failed to capture command $entity because ${ex.message}", ex)
            }
        }
    }

    protected open fun buildCommandEntity(request: HttpServletRequest) = createFrom(request)

    private fun parseBasic(request: HttpServletRequest): HttpRequestCommand {
        val requestHeaders = request.readHeaders()
        return HttpRequestCommand(
            sourceType = request.javaClass,
            url = request.requestURL.toString(),
            query = request.queryString,
            method = request.method,
            headers = requestHeaders,
        )
    }

    private fun createFrom(request: HttpServletRequest): HttpCommandEntity {
        return HttpCommandEntity.createFrom(parseBasic(request))
    }
}
