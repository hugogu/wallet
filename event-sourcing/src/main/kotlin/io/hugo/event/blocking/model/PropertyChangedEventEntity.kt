package io.hugo.event.blocking.model

import com.yahoo.elide.annotation.Include
import io.hugo.event.model.event.PropertyChangedEvent
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Include
@Entity
@DiscriminatorValue("DATA_CHANGED")
class PropertyChangedEventEntity : EventEntity<PropertyChangedEvent<*>>()
