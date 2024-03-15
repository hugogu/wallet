package io.hugo.event.model.http.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.hugo.common.serialization.SpringObjectMapperSupplier
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.SmartInitializingSingleton
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean

@AutoConfiguration
@EntityScan(basePackages = ["io.hugo.event.model.http"])
class EventSourcingHttpAutoConfiguration {
    @Bean
    fun objectMapperInitialization(mapper: ObjectMapper) = SmartInitializingSingleton {
        setupObjectMapper(mapper)
        if (mapper !== SpringObjectMapperSupplier.INSTANCE) {
            logger.warn("Two object mapper was detected. This may lead to inconsistent serialization.")
            setupObjectMapper(SpringObjectMapperSupplier.INSTANCE)
        }
    }

    private fun setupObjectMapper(mapper: ObjectMapper) {
        mapper.registerModule(EventSourcingJacksonModule())
        mapper.registerModule(HttpHeaderJacksonModule())
    }

    private val logger = LoggerFactory.getLogger(EventSourcingHttpAutoConfiguration::class.java)
}