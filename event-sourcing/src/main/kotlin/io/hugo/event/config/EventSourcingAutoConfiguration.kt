package io.hugo.event.config

import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@AutoConfiguration
class EventSourcingAutoConfiguration {

    @Configuration
    @ConditionalOnClass(ConnectionFactory::class)
    @EnableR2dbcRepositories("io.hugo.event.nio.dal")
    class EventSourcingAsyncAutoConfiguration {
    }
}
