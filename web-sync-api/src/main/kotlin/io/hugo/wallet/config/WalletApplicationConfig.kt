package io.hugo.wallet.config

import io.hugo.wallet.model.event.AccountBalanceActivity
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.util.function.Supplier

@Configuration
class WalletApplicationConfig {
    @Bean
    fun balanceActivitySupplier(): Supplier<Flux<AccountBalanceActivity>> = Supplier {
        BalanceSink.asFlux()
    }

    companion object {
        val BalanceSink: Sinks.Many<AccountBalanceActivity> = Sinks.many().multicast().onBackpressureBuffer()
    }
}
