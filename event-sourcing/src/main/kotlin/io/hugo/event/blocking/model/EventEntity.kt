package io.hugo.event.blocking.model

import com.fasterxml.jackson.databind.JsonNode
import io.hugo.common.model.EntityBase
import org.hibernate.annotations.Type
import java.time.Instant
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class EventEntity : EntityBase() {

    lateinit var commandId: UUID

    lateinit var aggregateId: UUID

    var eventType: String = ""

    var eventTime: Instant = Instant.EPOCH

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    lateinit var eventData: JsonNode
}
