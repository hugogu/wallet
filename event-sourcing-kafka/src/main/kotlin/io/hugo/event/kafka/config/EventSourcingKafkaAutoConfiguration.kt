package io.hugo.event.kafka.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.NamedType
import io.hugo.common.serialization.SpringObjectMapperSupplier
import io.hugo.event.kafka.DefaultRecordDataEventConverter
import io.hugo.event.kafka.EventSourcingProducerInterceptor
import io.hugo.event.kafka.KafkaMessageEvent
import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.beans.factory.SmartInitializingSingleton
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@AutoConfiguration
class EventSourcingKafkaAutoConfiguration {
    @Bean
    fun kafkaProducerFactorySourcingCustomizer(context: ApplicationContext) = DefaultKafkaProducerFactoryCustomizer {
        EventSourcingProducerInterceptor.context = context
        it.updateConfigs(
            mapOf(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG to "io.hugo.event.kafka.EventSourcingProducerInterceptor")
        )
    }

    @Bean
    @ConditionalOnMissingBean
    fun messageToEntityConverter() = DefaultRecordDataEventConverter<Any, Any>()

    @Bean
    fun objectMapperKafkaInitialization(mapper: ObjectMapper) = SmartInitializingSingleton {
        mapper.registerSubtypes(NamedType(KafkaMessageEvent::class.java, KafkaMessageEvent.TYPE))
        if (mapper !== SpringObjectMapperSupplier.INSTANCE) {
            SpringObjectMapperSupplier.INSTANCE.registerSubtypes(NamedType(KafkaMessageEvent::class.java, KafkaMessageEvent.TYPE))
        }
    }

    @Configuration
    @ConditionalOnMissingClass("io.r2dbc.spi.ConnectionFactory")
    @EntityScan(basePackages = ["io.hugo.event.kafka"])
    class EventSourcingSyncAutoConfiguration {
    }
}
