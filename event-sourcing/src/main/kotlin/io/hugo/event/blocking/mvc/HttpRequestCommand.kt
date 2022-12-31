package io.hugo.event.blocking.mvc

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import io.hugo.event.model.ExecutableCommand

@JsonPropertyOrder(
    value = ["method", "path", "headers"]
)
class HttpRequestCommand : ExecutableCommand {
    var sourceType: Class<*> = Any::class.java
    var method: String = ""
    var url: String = ""
    var body: String? = null
    var headers: Map<String, String> = emptyMap()
    val type = TYPE

    companion object {
        internal const val TYPE = "HttpRequest"
    }
}
