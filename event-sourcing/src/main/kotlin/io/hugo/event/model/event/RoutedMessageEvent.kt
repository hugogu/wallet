package io.hugo.event.model.event

import io.hugo.event.model.DomainEvent
import java.time.Instant

data class RoutedMessageEvent<V>(
    val source: String,
    val headers: Map<String, String> = emptyMap(),
    val message: V? = null,
    val timestamp: Instant = Instant.EPOCH,
    val routingParams: Map<String, Any?> = emptyMap(),
    override var eventTime: Instant = Instant.MAX,
) : DomainEvent {
    val type = TYPE

    companion object {
        const val TYPE = "OutgoingCommand"
    }
}
