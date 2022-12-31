package io.hugo.event.nio.dal

import io.hugo.event.nio.model.CommandEntity
import io.r2dbc.postgresql.codec.Json
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.UUID

@Repository
interface CommandRepo : R2dbcRepository<CommandEntity, UUID> {
    fun saveCommandData(data: String, type: String, id: UUID): Mono<CommandEntity> {
        val entity = CommandEntity().also {
            it.setId(id)
            it.commandType = type
            it.commandData = Json.of(data)
            it.commandTime = Instant.now()
            it.new = true
        }
        return save(entity)
    }
}
