package io.hugo.event.config

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.module.SimpleModule
import io.hugo.event.model.DomainEvent
import io.hugo.event.model.event.PropertyChangedEvent

class EventSourcingJacksonModule : SimpleModule("EventSourcing-Event",
    Version(0, 0, 1, null, "io.hugo", "common-jackson-event")
) {
    init {
        setMixInAnnotation(DomainEvent::class.java, DomainEventMixin::class.java)
    }

    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
    )
    @JsonSubTypes(
        JsonSubTypes.Type(value = PropertyChangedEvent::class, name = PropertyChangedEvent.TYPE),
    )
    class DomainEventMixin
}
