package io.hugo.event.blocking.model

import io.hugo.event.model.event.PropertyChangedEvent
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("DATA_CHANGED")
class PropertyChangedEventEntity : EventEntity<PropertyChangedEvent<*>>()
