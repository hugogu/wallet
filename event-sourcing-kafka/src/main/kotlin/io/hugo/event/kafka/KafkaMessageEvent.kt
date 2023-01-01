package io.hugo.event.kafka

import com.fasterxml.jackson.annotation.JsonIgnore
import io.hugo.event.model.DomainEvent
import java.time.Instant

data class KafkaMessageEvent<K, V>(
    val topic: String,
    val partition: Int? = null,
    val headers: Map<String, ByteArray> = emptyMap(),
    val key: K? = null,
    val value: V? = null,
    val timestamp: Long? = null,
    val configs: Map<String, Any?> = emptyMap(),
) : DomainEvent {
    val type = TYPE

    @get:JsonIgnore
    internal val messagingTime: Instant by lazy {
        Instant.ofEpochMilli(timestamp ?: System.currentTimeMillis())
    }

    companion object {
        const val TYPE = "OutgoingCommand"
    }
}
