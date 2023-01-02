package io.hugo.event.kafka.service

import io.hugo.event.blocking.model.RoutedMessageEventEntity
import io.hugo.event.kafka.RecordDataEventConverter
import io.hugo.event.model.internal.ReplayCommand
import org.springframework.context.event.EventListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class CommandReplayListener(
    private val kafkaTemplate: KafkaTemplate<Any, Any>,
    private val converter: RecordDataEventConverter<Any, Any>,
) {
    @EventListener(
        condition = "#command.entity instanceof T(io.hugo.event.blocking.model.RoutedMessageEventEntity)"
    )
    fun onReplayCommand(command: ReplayCommand<*>) {
        assert(command.entity is RoutedMessageEventEntity)
        val entity = command.entity as RoutedMessageEventEntity
        val record = converter.convertBack(entity)
        record.headers().add(REPLAY_MARK, "true".toByteArray())

        kafkaTemplate.send(record)
    }

    companion object {
        const val REPLAY_MARK = "event.sourcing.replay"
    }
}
