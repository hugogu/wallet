package io.hugo.wallet.config

import io.hugo.wallet.model.event.AccountBalanceActivity
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.util.function.Supplier

@Configuration
class WalletApplicationConfig {
    private val sink: Sinks.Many<AccountBalanceActivity> = Sinks.many().multicast().onBackpressureBuffer()

    @EventListener
    fun balanceActivityEventListener(activity: AccountBalanceActivity) {
        sink.tryEmitNext(activity)
    }

    @Bean
    fun balanceActivitySupplier(): Supplier<Flux<AccountBalanceActivity>> = Supplier {
        sink.asFlux()
    }
}
