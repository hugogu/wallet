package io.hugo.wallet.config

import io.hugo.wallet.config.EventTopics.BALANCE_TOPIC
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WalletApplicationConfig {
    @Bean
    fun callBackTopic() = NewTopic(BALANCE_TOPIC, 1, 1)
}
