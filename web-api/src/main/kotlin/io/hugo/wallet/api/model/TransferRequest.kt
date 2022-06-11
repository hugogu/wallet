package io.hugo.wallet.api.model

import io.hugo.wallet.model.TransactionEntity
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class TransferRequest(
    val from: UUID = UUID(0, 0),
    val to: UUID = UUID(0, 0),
    val amount: BigDecimal = BigDecimal.ZERO,
    val currency: String = "",
) {
    fun toEntity(id: UUID) = TransactionEntity().also {
        it.id = id
        it.amount = amount
        it.currency = currency
        it.fromAccount = from
        it.toAccount = to
        it.transactionTime = Instant.now()
    }
}
