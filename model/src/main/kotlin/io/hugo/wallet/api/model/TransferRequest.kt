package io.hugo.wallet.api.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.javamoney.moneta.Money
import java.util.UUID
import javax.money.Monetary
import javax.money.MonetaryAmount

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
data class TransferRequest(
    val from: UUID = UUID(0, 0),
    val to: UUID = UUID(0, 0),
    val monetary: MonetaryAmount = Money.zero(Monetary.getCurrency("XXX")),
)
