package io.hugo.event.nio.model

import io.hugo.common.model.R2EntityBase
import io.r2dbc.postgresql.codec.Json
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table("event")
class EventEntity : R2EntityBase() {

    lateinit var commandId: UUID

    lateinit var aggregateId: UUID

    var eventType: String = ""

    var eventTime: Instant = Instant.EPOCH

    lateinit var eventData: Json
}
