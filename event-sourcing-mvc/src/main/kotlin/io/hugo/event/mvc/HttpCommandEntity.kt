package io.hugo.event.mvc

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.hugo.common.mvc.HttpServletRequestUtils.readHeaders
import io.hugo.event.blocking.model.CommandEntity
import io.hugo.event.mvc.HttpRequestCommand.Companion.REQUEST_ID
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import org.slf4j.LoggerFactory
import java.util.UUID
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.servlet.http.HttpServletRequest

@Entity
@DiscriminatorValue("HTTP_REQUEST")
@TypeDefs(TypeDef(name = "jsonb", typeClass = JsonBinaryType::class))
class HttpCommandEntity : CommandEntity<HttpRequestCommand>() {
    fun setBody(body: String) {
        commandData = commandData.copy(body = body)
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
            entity.commandData = HttpRequestCommand.parseBasic(request)

            return entity
        }

        private val logger = LoggerFactory.getLogger(HttpCommandEntity::class.java)
    }
}
