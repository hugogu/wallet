package io.hugo.event.webflux

import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class CacheServerHttpRequestDecorator(
    delegate: ServerHttpRequest,
) : ServerHttpRequestDecorator(delegate) {
    private var bytes = ByteArray(0)

    private val body: Mono<ByteArray> = DataBufferUtils.join(delegate.body).map {
        bytes = ByteArray(it.readableByteCount())
        it.read(bytes)
        bytes
    }

    fun getBodyString(): String {
        return String(bytes)
    }

    override fun getBody(): Flux<DataBuffer> {
        return body.map {
            DefaultDataBufferFactory().wrap(it)
        }.flatMapMany {
            Flux.just(it)
        }
    }
}
