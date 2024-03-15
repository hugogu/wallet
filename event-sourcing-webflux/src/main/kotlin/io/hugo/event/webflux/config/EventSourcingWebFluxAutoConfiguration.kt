package io.hugo.event.webflux.config

import io.hugo.event.blocking.dal.CommandRepo
import io.hugo.event.webflux.CommandCaptureWebFilter
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean

@AutoConfiguration
class EventSourcingWebFluxAutoConfiguration {
    @Bean
    fun requestSourcingFilter(commandRepo: CommandRepo): CommandCaptureWebFilter {
        return CommandCaptureWebFilter(commandRepo)
    }
}
