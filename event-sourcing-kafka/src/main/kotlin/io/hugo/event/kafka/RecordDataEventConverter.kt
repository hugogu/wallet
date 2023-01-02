package io.hugo.event.kafka

import io.hugo.event.blocking.model.RoutedMessageEventEntity
import io.hugo.event.model.EventSourcingContext
import io.hugo.event.model.event.RoutedEventSource
import io.hugo.event.model.event.RoutedMessageEvent
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.internals.RecordHeader
import java.time.Instant
import java.util.UUID

@FunctionalInterface
interface RecordDataEventConverter<K, V> {
    fun convert(
        record: ProducerRecord<K, V>,
        configs: Map<String, Any?>
    ): RoutedMessageEventEntity

    fun convertBack(entity: RoutedMessageEventEntity): ProducerRecord<K, V>
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
                TOPIC to record.topic(),
                PARTITION to record.partition(),
                KEY to record.key(),
            ),
        )
        entity.new = true
        entity.setId(UUID.nameUUIDFromBytes(eventId.toByteArray()))
        entity.eventData = kafkaEvent
        entity.eventTime = kafkaEvent.timestamp
        entity.commandId = EventSourcingContext.getCurrentCommand()?.id

        return entity
    }

    override fun convertBack(entity: RoutedMessageEventEntity): ProducerRecord<K, V> {
        val event = entity.eventData
        val topic = event.routingParams[TOPIC].toString()
        val partition = event.routingParams[PARTITION]?.let { Integer.parseInt(it.toString()) }
        val key = event.routingParams[KEY] as K
        val headers = event.headers.map { RecordHeader(it.key, it.value.toByteArray()) }

        return ProducerRecord<K, V>(topic, partition, event.timestamp.toEpochMilli(), key, event.message as V, headers)
    }

    companion object {
        private const val TOPIC = "topic"
        private const val PARTITION = "partition"
        private const val KEY = "key"
    }
}
