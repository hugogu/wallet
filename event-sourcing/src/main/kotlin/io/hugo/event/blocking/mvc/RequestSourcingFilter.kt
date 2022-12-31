package io.hugo.event.blocking.mvc

import io.hugo.event.blocking.dal.CommandRepo
import io.hugo.event.blocking.model.HttpCommandEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RequestSourcingFilter : OncePerRequestFilter() {
    @Autowired
    private lateinit var commandRepo: CommandRepo

    var exclusivePaths = listOf("/operation")

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (exclusivePaths.any { request.requestURI.startsWith(it) }) {
            return filterChain.doFilter(request, response)
        }

        val entity = HttpCommandEntity.createFrom(request)
        val cachedRequest = ContentCachingRequestWrapper(request)

        try {
            filterChain.doFilter(cachedRequest, response)
        } finally {
            entity.setBody(String(cachedRequest.contentAsByteArray))
            commandRepo.saveAndFlush(entity)
        }
    }
}
