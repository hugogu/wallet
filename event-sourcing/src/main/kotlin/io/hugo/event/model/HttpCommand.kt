package io.hugo.event.model

import org.springframework.http.HttpHeaders

data class HttpCommand(
    val headers: HttpHeaders = HttpHeaders.EMPTY,
    val url: String = "",
    val body: String = "",
)
