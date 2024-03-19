package io.hugo.wallet.model.event

import java.util.*

data class FraudAlert(
    val accountId: UUID = UUID(0, 0),
    val transactionId: UUID = UUID(0, 0),
)
