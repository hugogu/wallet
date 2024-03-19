package io.hugo.wallet.model

import org.javamoney.moneta.FastMoney
import java.util.*
import javax.money.MonetaryAmount

data class TransactionSpec(
    /**
     * Will be used as the Id of TransactionEntity.
     */
    val id: UUID = UUID(0, 0),
    val fromAccount: UUID = UUID(0, 0),
    val toAccount: UUID = UUID(0, 0),
    val amount: MonetaryAmount = FastMoney.MIN_VALUE
)
