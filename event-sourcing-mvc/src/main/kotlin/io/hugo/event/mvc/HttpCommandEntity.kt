package io.hugo.event.mvc

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.hugo.event.blocking.model.CommandEntity
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
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
            val command = HttpRequestCommand.parseBasic(request)
            val entity = HttpCommandEntity()
            entity.new = true
            entity.setId(command.id)
            entity.commandData = command

            return entity
        }
    }
}
