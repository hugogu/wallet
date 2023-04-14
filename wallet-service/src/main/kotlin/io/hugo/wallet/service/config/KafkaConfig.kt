package io.hugo.wallet.service.config

import io.hugo.common.messaging.HttpSourcedMessage
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.event.EventListener
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.function.Supplier

@Configuration
@Import(KafkaAutoConfiguration::class)
class KafkaConfig {
    private val httpMessages: BlockingQueue<HttpSourcedMessage> = LinkedBlockingQueue()

    @EventListener
    fun callbackEventListener(message: HttpSourcedMessage) {
        httpMessages.add(message)
    }

    @Bean
    fun httpMessageSupplier(): Supplier<HttpSourcedMessage> = Supplier {
        httpMessages.take()
    }
}
