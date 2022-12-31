package io.hugo.common.messaging

data class HttpSourcedMessage(
    val headers: Map<String, String> = emptyMap(),
    val url: String = "",
    val body: String = "",
    val method: String = "",
)
