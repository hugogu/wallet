package io.hugo.event.model.event

import io.hugo.event.model.DomainEvent
import java.time.Instant

data class PropertyChangedEvent<V>(
    val property: String,
    val newValue: V? = null,
    override var eventTime: Instant = Instant.MAX,
) : DomainEvent {
    val type = TYPE

    companion object {
        const val TYPE = "PropertyChanged"
    }
}
