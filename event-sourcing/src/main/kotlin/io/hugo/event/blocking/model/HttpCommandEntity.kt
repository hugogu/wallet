package io.hugo.event.blocking.model

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.hugo.common.mvc.HttpServletRequestUtils.readHeaders
import io.hugo.event.blocking.mvc.HttpRequestCommand
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.util.UUID
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.servlet.http.HttpServletRequest

@Entity
@DiscriminatorValue("HTTP_REQUEST")
@TypeDefs(TypeDef(name = "jsonb", typeClass = JsonBinaryType::class))
class HttpCommandEntity : CommandEntity<HttpRequestCommand>() {

    override fun <T> executeWith(template: RestTemplate, clazz: Class<T>): ResponseEntity<T> {
        val requestData = commandData
        val uri = URI.create(requestData.url)
        val httpMethod = HttpMethod.valueOf(requestData.method)
        val httpHeaders = HttpHeaders().apply {
            requestData.headers.forEach(::add)
        }
        val request = RequestEntity<String>(httpHeaders, httpMethod, uri)

        return template.exchange(request, clazz)
    }

    fun setBody(body: String) {
        (commandData).body = body
    }

    companion object {
        fun createFrom(request: HttpServletRequest): HttpCommandEntity {
            val requestHeaders = request.readHeaders()
            val commandId = requestHeaders[REQUEST_ID]?.firstOrNull()?.let {
                UUID.nameUUIDFromBytes((it + request.requestURI).toByteArray())
            }
            val entity = HttpCommandEntity()
            entity.new = true
            entity.setId(commandId ?: run {
                logger.warn("No request id found in HTTP request, fail back to generation.")
                UUID.randomUUID()
            })
            entity.commandData = HttpRequestCommand().apply {
                sourceType = request.javaClass
                url = request.requestURL.toString()
                method = request.method
                headers = requestHeaders.map { it.key to it.value.first() }.toMap()
            }

            return entity
        }

        const val REQUEST_ID = "x-request-id"

        private val logger = LoggerFactory.getLogger(HttpCommandEntity::class.java)
    }
}
