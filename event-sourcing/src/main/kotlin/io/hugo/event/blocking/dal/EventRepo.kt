package io.hugo.event.blocking.dal

import io.hugo.event.blocking.model.EventEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface EventRepo : JpaRepository<EventEntity, UUID> {
}
