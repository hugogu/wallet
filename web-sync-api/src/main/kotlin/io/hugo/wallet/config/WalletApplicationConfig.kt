package io.hugo.wallet.config

import io.hugo.wallet.model.event.AccountBalanceActivity
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.util.function.Supplier

@Configuration
class WalletApplicationConfig {
    private val sink: Sinks.Many<AccountBalanceActivity> = Sinks.many().multicast().onBackpressureBuffer()

    // it should be before commit because it should be persisted along with the business transaction.
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun balanceActivityEventListener(activity: AccountBalanceActivity) {
        // There is no DB storage for balance activity.
        // It is not hard to build it by consumer the external message broker when we need one.
        // Just send the event to the sink (external message broker) for now.
        sink.tryEmitNext(activity)
    }

    @Bean
    fun balanceActivitySupplier(): Supplier<Flux<AccountBalanceActivity>> = Supplier {
        sink.asFlux()
    }
}
