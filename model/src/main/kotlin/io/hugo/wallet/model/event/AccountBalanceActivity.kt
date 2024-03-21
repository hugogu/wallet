package io.hugo.wallet.model.event

import org.javamoney.moneta.FastMoney
import java.time.Instant
import java.util.UUID
import javax.money.MonetaryAmount

data class AccountBalanceActivity(
    val transactionId: UUID = UUID(0, 0),
    val accountId: UUID = UUID(0, 0),
    val amount: MonetaryAmount = FastMoney.MIN_VALUE,
    val occurringTime: Instant = Instant.EPOCH
)
