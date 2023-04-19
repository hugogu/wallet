package io.hugo.event.stream

import io.hugo.event.blocking.model.RoutedMessageEventEntity
import io.hugo.event.model.EventSourcingContext
import io.hugo.event.model.event.RoutedMessageEvent
import org.springframework.messaging.Message
import java.time.Instant

interface ChannelMessageConverter {
    fun convert(message: Message<*>): RoutedMessageEventEntity

    fun convertBack(entity: RoutedMessageEventEntity): Message<*>
}


class DefaultChannelMessageConverter : ChannelMessageConverter {
    override fun convert(message: Message<*>): RoutedMessageEventEntity {
        val entity = RoutedMessageEventEntity()
        val eventId = requireNotNull(message.headers.id)
        val kafkaEvent = RoutedMessageEvent(
            source = SOURCE,
            headers = message.headers.map { it.key to it.value.toString() }.toMap(),
            message = message.payload,
            timestamp = Instant.ofEpochMilli(message.headers.timestamp ?: System.currentTimeMillis()),
            routingParams = mapOf(
                // TODO: Get all related information into it.
                // This is actually impossible due to missing information in the message.
                TOPIC to message.headers[TOPIC],
            )
        )
        entity.new = true
        entity.setId(eventId)
        entity.eventData = kafkaEvent
        entity.eventTime = kafkaEvent.timestamp
        entity.commandId = EventSourcingContext.getCurrentCommand()?.id

        return entity
    }

    override fun convertBack(entity: RoutedMessageEventEntity): Message<*> {
        TODO("Not yet implemented")
    }


    companion object {
        private const val TOPIC = "topic"
        private const val PARTITION = "partition"
        private const val KEY = "key"
        private const val SOURCE = "stream"
    }
}
