package io.hugo.wallet.service.config

import io.hugo.common.messaging.HttpSourcedMessage
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.event.EventListener
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.util.function.Supplier

@Configuration
@Import(KafkaAutoConfiguration::class)
class KafkaConfig {
    private val sink: Sinks.Many<HttpSourcedMessage> = Sinks.many().multicast().onBackpressureBuffer()

    @EventListener
    fun callbackEventListener(message: HttpSourcedMessage) {
        sink.tryEmitNext(message)
    }

    @Bean
    fun httpMessageSupplier(): Supplier<Flux<HttpSourcedMessage>> = Supplier {
        sink.asFlux()
    }
}
