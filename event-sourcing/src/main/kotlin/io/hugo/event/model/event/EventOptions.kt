package io.hugo.event.model.event

data class EventOptions(
    val mode: EventPublishTarget = EventPublishTarget.APP_EVENT,
)

enum class EventPublishTarget {
    APP_EVENT,
    KAFKA,
    RABBITMQ,
    STREAM_SINK,
}
