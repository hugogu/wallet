package io.hugo.event.stream.config

import io.hugo.event.stream.DefaultChannelMessageConverter
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan

@AutoConfiguration
@ComponentScan("io.hugo.event.stream")
class EventSourcingStreamAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun streamToEntityConverter() = DefaultChannelMessageConverter()
}
