package io.hugo.event.webflux

import io.hugo.event.model.http.HttpCommandEntity
import io.hugo.event.model.http.HttpRequestCommand
import org.springframework.http.HttpMethod
import org.springframework.http.server.reactive.ServerHttpRequest

object HttpCommandUtils {
    fun createFrom(request: ServerHttpRequest): HttpCommandEntity {
        return HttpCommandEntity.createFrom(parseBasic(request))
    }

    fun parseBasic(request: ServerHttpRequest): HttpRequestCommand {
        val requestHeaders = request.headers
        return HttpRequestCommand(
            sourceType = request.javaClass,
            url = request.uri.toString(),
            query = request.queryParams.toString(),
            method = request.method?.name ?: HttpMethod.GET.name,
            headers = requestHeaders,
        )
    }
}