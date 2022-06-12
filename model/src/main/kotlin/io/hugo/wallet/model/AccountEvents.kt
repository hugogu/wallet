package io.hugo.wallet.model

import java.util.UUID
import javax.money.MonetaryAmount

data class AccountDebitEvent(
    val monetary: MonetaryAmount,
    val accountId: UUID = UUID(0, 0),
)

data class AccountCreditEvent(
    val monetary: MonetaryAmount,
    val accountId: UUID = UUID(0, 0),
)
