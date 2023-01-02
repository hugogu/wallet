package io.hugo.event.model.event

import io.hugo.event.model.DomainEvent

data class PropertyChangedEvent<V>(
    val property: String,
    val newValue: V? = null
) : DomainEvent {
    val type = TYPE

    companion object {
        const val TYPE = "PropertyChanged"
    }
}
