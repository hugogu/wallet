package io.hugo.event.nio.dal

import io.hugo.event.nio.model.EventEntity
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface EventRepo : R2dbcRepository<EventEntity, UUID> {
}
