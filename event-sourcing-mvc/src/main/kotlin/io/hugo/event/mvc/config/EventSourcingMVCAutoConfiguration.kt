package io.hugo.event.mvc.config

import io.hugo.event.mvc.RequestSourcingFilter
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@AutoConfiguration
class EventSourcingMVCAutoConfiguration {
    @Configuration
    @ConditionalOnMissingClass("io.r2dbc.spi.ConnectionFactory")
    class EventSourcingSyncAutoConfiguration {
        @Bean
        fun mvcRequestCapture() = RequestSourcingFilter()
    }
}
