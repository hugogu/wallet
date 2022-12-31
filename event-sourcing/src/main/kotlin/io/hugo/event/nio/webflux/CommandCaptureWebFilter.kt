package io.hugo.event.nio.webflux

import io.hugo.event.nio.dal.CommandRepo
import org.springframework.stereotype.Service
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Service
class CommandCaptureWebFilter(
    private val commandRepo: CommandRepo,
) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return chain.filter(exchange).doFinally {
        }
    }
}
