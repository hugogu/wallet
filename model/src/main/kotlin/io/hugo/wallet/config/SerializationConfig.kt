package io.hugo.wallet.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.hugo.common.serialization.SpringObjectMapperSupplier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SerializationConfig {
    @Bean
    fun objectMapper(): ObjectMapper = SpringObjectMapperSupplier.INSTANCE
}
