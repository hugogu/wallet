package io.hugo.event.mvc

import io.hugo.event.blocking.model.CommandEntity
import java.time.Instant
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.servlet.http.HttpServletRequest

@Entity
@DiscriminatorValue("HTTP_REQUEST")
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
            entity.commandTime = command.getCommandTime()?.toInstant() ?: Instant.now()

            return entity
        }
    }
}
