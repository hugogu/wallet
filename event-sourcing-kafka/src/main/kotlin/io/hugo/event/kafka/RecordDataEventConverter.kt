package io.hugo.event.kafka

import io.hugo.event.model.EventSourcingContext
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.UUID

@FunctionalInterface
interface RecordDataEventConverter<K, V> {
    fun convert(
        record: ProducerRecord<K, V>,
        configs: Map<String, Any?>
    ): KafkaMessageEventEntity
}

class DefaultRecordDataEventConverter<K, V> : RecordDataEventConverter<K, V> {

    override fun convert(
        record: ProducerRecord<K, V>,
        configs: Map<String, Any?>
    ): KafkaMessageEventEntity {
        val entity = KafkaMessageEventEntity()
        val eventId = "${record.topic()}${record.key()}${record.timestamp()}"
        val kafkaEvent = KafkaMessageEvent(
            topic =  record.topic(),
            partition = record.partition(),
            headers = record.headers().associate { it.key() to it.value() },
            key = record.key(),
            value = record.value(),
            timestamp = record.timestamp(),
            configs = configs,
        )
        entity.new = true
        entity.setId(UUID.nameUUIDFromBytes(eventId.toByteArray()))
        entity.eventData = kafkaEvent
        entity.eventTime = kafkaEvent.messagingTime
        entity.commandId = EventSourcingContext.getCurrentCommand()!!.id

        return entity
    }
}
