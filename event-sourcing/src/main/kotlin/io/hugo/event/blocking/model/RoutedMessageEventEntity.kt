package io.hugo.event.blocking.model

import io.hugo.event.model.event.RoutedMessageEvent
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.persistence.Transient

@Entity
@DiscriminatorValue("ROUTED_MESSAGE")
class RoutedMessageEventEntity : EventEntity<RoutedMessageEvent<*>>() {
    @Transient
    override fun getEvent(): Any {
        return eventData.message!!
    }
}
