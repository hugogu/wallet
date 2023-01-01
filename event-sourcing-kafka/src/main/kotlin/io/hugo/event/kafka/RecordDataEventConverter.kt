package io.hugo.event.kafka

import io.hugo.event.blocking.model.RoutedMessageEventEntity
import io.hugo.event.model.EventSourcingContext
import io.hugo.event.model.event.RoutedEventSource
import io.hugo.event.model.event.RoutedMessageEvent
import org.apache.kafka.clients.producer.ProducerRecord
import java.time.Instant
import java.util.UUID

@FunctionalInterface
interface RecordDataEventConverter<K, V> {
    fun convert(
        record: ProducerRecord<K, V>,
        configs: Map<String, Any?>
    ): RoutedMessageEventEntity
}

class DefaultRecordDataEventConverter<K, V> : RecordDataEventConverter<K, V> {

    override fun convert(
        record: ProducerRecord<K, V>,
        configs: Map<String, Any?>
    ): RoutedMessageEventEntity {
        val entity = RoutedMessageEventEntity()
        val eventId = "${record.topic()}${record.key()}${record.value().toString()}${record.value().hashCode()}"
        val kafkaEvent = RoutedMessageEvent(
            sourceType = RoutedEventSource.KAFKA,
            headers = record.headers().associate { it.key() to String(it.value()) },
            message = record.value(),
            timestamp = Instant.ofEpochMilli(record.timestamp() ?: System.currentTimeMillis()),
            routingParams = configs + mapOf(
                "topic" to record.topic(),
                "partition" to record.partition(),
                "key" to record.key(),
            ),
        )
        entity.new = true
        entity.setId(UUID.nameUUIDFromBytes(eventId.toByteArray()))
        entity.eventData = kafkaEvent
        entity.eventTime = kafkaEvent.timestamp
        entity.commandId = EventSourcingContext.getCurrentCommand()!!.id

        return entity
    }
}
