package io.hugo.event.model.event

import io.hugo.event.model.DomainEvent

class PropertyChangedEvent : DomainEvent {
    companion object {
        const val TYPE = "PropertyChanged"
    }
}
