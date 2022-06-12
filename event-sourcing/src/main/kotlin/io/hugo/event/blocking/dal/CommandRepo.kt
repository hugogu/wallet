package io.hugo.event.blocking.dal

import io.hugo.event.blocking.model.CommandEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CommandRepo : JpaRepository<CommandEntity, UUID> {
}
