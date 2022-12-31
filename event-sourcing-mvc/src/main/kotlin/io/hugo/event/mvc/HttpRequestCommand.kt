package io.hugo.event.mvc

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import io.hugo.common.mvc.HttpServletRequestUtils.readHeaders
import io.hugo.event.model.CommandOptions
import io.hugo.event.model.ExecutableCommand
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.web.client.RestTemplate
import java.net.InetAddress
import java.net.URI
import java.util.UUID
import javax.servlet.http.HttpServletRequest

@JsonPropertyOrder(
    value = ["type", "method", "path", "headers"]
)
data class HttpRequestCommand(
    /**
     * The source refers to the original data used to capture this [HttpRequestCommand]
     * This [sourceType] refers to the data class of that source.
     * In case of embedded tomcat, it is `org.apache.catalina.connector.RequestFacade` .
     */
    val sourceType: Class<*> = Any::class.java,
    val method: String = "",
    val url: String = "",
    val body: String? = null,
    val headers: Map<String, String> = emptyMap(),
) : ExecutableCommand {
    val type = TYPE

    override fun execute(options: CommandOptions): Any {
        val uri = URI.create(url)
        val httpMethod = HttpMethod.valueOf(method)
        val httpHeaders = HttpHeaders().apply {
            headers.forEach(::add)
        }
        httpHeaders.add(FORWARDED_FOR, httpHeaders.getFirst(HttpHeaders.HOST))
        httpHeaders.add(HttpHeaders.HOST, InetAddress.getLocalHost().hostName)
        if (options.renewRequestIds) {
            // TODO: Remove random UUID.
            httpHeaders.add(REQUEST_ID, UUID.randomUUID().toString())
        }
        val request = RequestEntity<String>(httpHeaders, httpMethod, uri)

        return RestTemplate().exchange(request, Any::class.java)
    }

    companion object {
        internal const val TYPE = "HttpRequest"
        internal const val REQUEST_ID = "X-Request-ID"
        internal const val FORWARDED_FOR = "X-Forwarded-For"

        fun parseBasic(request: HttpServletRequest): HttpRequestCommand {
            val requestHeaders = request.readHeaders()
            return HttpRequestCommand(
                sourceType = request.javaClass,
                url = request.requestURL.toString(),
                method = request.method,
                headers = requestHeaders.map { it.key to it.value.first() }.toMap(),
            )
        }

        private val logger = LoggerFactory.getLogger(HttpRequestCommand::class.java)
    }
}
