package io.hugo.event.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.hugo.common.serialization.SpringObjectMapperSupplier
import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.SmartInitializingSingleton
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@AutoConfiguration
class EventSourcingAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun objectMapper(context: ApplicationContext) = SpringObjectMapperSupplier.INSTANCE

    @Bean
    fun objectMapperEventInitialization(mapper: ObjectMapper) = SmartInitializingSingleton {
        mapper.registerModule(EventSourcingJacksonModule())
        if (mapper !== SpringObjectMapperSupplier.INSTANCE) {
            SpringObjectMapperSupplier.INSTANCE.registerModule(EventSourcingJacksonModule())
        }
    }

    @Configuration
    @ConditionalOnClass(ConnectionFactory::class)
    @EnableR2dbcRepositories("io.hugo.event.nio.dal")
    class EventSourcingAsyncAutoConfiguration {
    }

    @Configuration
    @ConditionalOnMissingClass("io.r2dbc.spi.ConnectionFactory")
    @EnableJpaRepositories("io.hugo.event.blocking.dal")
    @EntityScan(basePackages = ["io.hugo.event.blocking.model"])
    @ComponentScan(basePackages = ["io.hugo.event.blocking.controller"])
    class EventSourcingSyncAutoConfiguration {
    }
}
