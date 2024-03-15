package io.hugo.event.webflux

import io.hugo.event.blocking.dal.CommandRepo
import io.hugo.event.model.EventSourcingContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Service
class CommandCaptureWebFilter(
    private val commandRepo: CommandRepo,
) : WebFilter {
    var exclusivePaths = listOf("/actuator")

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request

        if (exclusivePaths.any { request.uri.path.startsWith(it) }) {
            return chain.filter(exchange)
        }

        val entity = HttpCommandUtils.createFrom(request)
        EventSourcingContext.setCurrentCommand(entity.commandData)
        val cachedExchange = exchange.mutate().request(CacheServerHttpRequestDecorator(request)).build()

        return chain.filter(cachedExchange).doFinally {
            EventSourcingContext.setCurrentCommand(null)
            val cachedRequest = cachedExchange.request as CacheServerHttpRequestDecorator
            entity.setBody(cachedRequest.getBodyString())
            try {
                // TODO: Switch to non-blocking solution
                commandRepo.saveAndFlush(entity)
            } catch (ex: Exception) {
                logger.error("Failed to capture command $entity because ${ex.message}", ex)
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CommandCaptureWebFilter::class.java)
    }
}
