package io.hugo.wallet.config

import org.springframework.boot.web.codec.CodecCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WebFluxConfiguration {
    @Bean
    fun codecConfigure() = CodecCustomizer {
        it.defaultCodecs().enableLoggingRequestDetails(true)
    }
}
