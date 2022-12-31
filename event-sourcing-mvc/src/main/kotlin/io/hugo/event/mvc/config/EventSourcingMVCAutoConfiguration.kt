package io.hugo.event.mvc.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.hugo.common.serialization.SpringObjectMapperSupplier
import io.hugo.event.mvc.RequestSourcingFilter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.SmartInitializingSingleton
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@AutoConfiguration
class EventSourcingMVCAutoConfiguration {
    @Bean
    fun objectMapperInitialization(mapper: ObjectMapper) = SmartInitializingSingleton {
        setupObjectMapper(mapper)
        if (mapper !== SpringObjectMapperSupplier.INSTANCE) {
            logger.warn("Two object mapper was detected. This may lead to inconsistent serialization.")
            setupObjectMapper(SpringObjectMapperSupplier.INSTANCE)
        }
    }

    @Configuration
    @EntityScan(basePackages = ["io.hugo.event.mvc"])
    @ConditionalOnMissingClass("io.r2dbc.spi.ConnectionFactory")
    class EventSourcingSyncAutoConfiguration {
        @Bean
        fun mvcRequestCapture() = RequestSourcingFilter()
    }

    private fun setupObjectMapper(mapper: ObjectMapper) {
        mapper.registerModule(EventSourcingJacksonModule())
        mapper.registerModule(HttpHeaderJacksonModule())
    }

    private val logger = LoggerFactory.getLogger(EventSourcingMVCAutoConfiguration::class.java)
}
