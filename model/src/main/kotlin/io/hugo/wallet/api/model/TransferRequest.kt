package io.hugo.wallet.api.model

import org.javamoney.moneta.Money
import java.util.UUID
import javax.money.Monetary
import javax.money.MonetaryAmount

data class TransferRequest(
    val from: UUID = UUID(0, 0),
    val to: UUID = UUID(0, 0),
    val monetary: MonetaryAmount = Money.zero(Monetary.getCurrency("XXX")),
)
