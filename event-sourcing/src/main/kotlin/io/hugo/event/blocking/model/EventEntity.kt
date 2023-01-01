package io.hugo.event.blocking.model

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.hugo.common.model.EntityBase
import io.hugo.event.model.DomainEvent
import io.hugo.event.model.event.PropertyChangedEvent
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.time.Instant
import java.util.UUID
import javax.persistence.Column
import javax.persistence.DiscriminatorColumn
import javax.persistence.DiscriminatorType
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.Table

@Entity
@Table(name = "event")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name = "event_type",
    discriminatorType = DiscriminatorType.STRING,
    length = 16,
)
@TypeDefs(TypeDef(name = "jsonb", typeClass = JsonBinaryType::class))
open class EventEntity<T: DomainEvent> : EntityBase() {

    lateinit var commandId: UUID

    lateinit var aggregateId: UUID

    var eventTime: Instant = Instant.EPOCH

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    lateinit var eventData: T
}

@Entity
@DiscriminatorValue("DATA_CHANGED")
class DomainDataEventEntity : EventEntity<PropertyChangedEvent>()
