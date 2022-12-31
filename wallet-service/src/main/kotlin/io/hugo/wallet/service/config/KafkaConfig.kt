package io.hugo.wallet.service.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(KafkaAutoConfiguration::class)
class KafkaConfig {
    @Bean
    fun callBackTopic() = NewTopic("Callbacks", 1, 1)
}
