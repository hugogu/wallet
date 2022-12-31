package io.hugo.event.mvc

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import io.hugo.common.mvc.HttpServletRequestUtils.readHeaders
import io.hugo.event.model.CommandOptions
import io.hugo.event.model.ExecutableCommand
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.net.InetAddress
import java.net.URI
import java.time.ZonedDateTime
import java.util.UUID
import javax.servlet.http.HttpServletRequest

@JsonPropertyOrder(
    value = ["type", "sourceType", "method", "path", "headers"]
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
    val query: String? = null,
    val body: String? = null,

    /**
     * The headers have to be immutable to yield a stable id.
     */
    val headers: MultiValueMap<String, String> = HttpHeaders(),
) : ExecutableCommand {
    val type = TYPE

    override val id: UUID by lazy {
        headers[REQUEST_ID.lowercase()]?.firstOrNull()?.let {
            UUID.nameUUIDFromBytes((it + url).toByteArray())
        } ?: run {
            logger.warn("No request id found in HTTP request, fail back to generation.")
            UUID.randomUUID()
        }
    }

    @JsonIgnore
    fun getCommandTime(): ZonedDateTime? {
        return try {
            HttpHeaders(headers).getFirstZonedDateTime(HttpHeaders.DATE)
        } catch (ex: IllegalArgumentException) {
            logger.warn(ex.message)
            null
        }
    }

    @get:JsonIgnore
    internal val fullUri: URI by lazy {
        URI.create("$url?${query.orEmpty()}")
    }

    override fun execute(options: CommandOptions): Any {
        val httpMethod = HttpMethod.valueOf(method)
        val httpHeaders = HttpHeaders().apply {
            addAll(headers)
        }
        httpHeaders.add(FORWARDED_FOR, httpHeaders.getFirst(HttpHeaders.HOST))
        httpHeaders[HttpHeaders.HOST] = InetAddress.getLocalHost().hostName
        if (options.renewRequestIds) {
            httpHeaders[REQUEST_ID] = listOf(UUID.nameUUIDFromBytes(options.randomSeed.toByteArray()).toString())
        }
        val request = RequestEntity<String>(body, httpHeaders, httpMethod, fullUri)

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
                query = request.queryString,
                method = request.method,
                headers = requestHeaders,
            )
        }

        private val logger = LoggerFactory.getLogger(HttpRequestCommand::class.java)
    }
}
