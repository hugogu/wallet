package io.hugo.wallet.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cloud.stream.binder.kafka.config.ClientFactoryCustomizer
import org.springframework.context.annotation.Primary
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.stereotype.Service

@Primary
@Service
class KafkaStreamClientFactoryCustomizer(private val objectMapper: ObjectMapper) : ClientFactoryCustomizer {
    override fun configure(cf: ConsumerFactory<*, *>?) {
        super.configure(cf)
    }

    override fun configure(pf: ProducerFactory<*, *>?) {
        if (pf is DefaultKafkaProducerFactory<*, *>) {
            pf.valueSerializer = JsonSerializer(objectMapper)
        }
    }
}
