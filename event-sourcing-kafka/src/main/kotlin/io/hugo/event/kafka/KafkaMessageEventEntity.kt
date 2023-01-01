package io.hugo.event.kafka

import io.hugo.event.blocking.model.EventEntity
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("KAFKA_MESSAGE")
class KafkaMessageEventEntity : EventEntity<KafkaMessageEvent<*, *>>() {
}
