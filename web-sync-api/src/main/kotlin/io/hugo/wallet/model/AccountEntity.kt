package io.hugo.wallet.model

import io.hugo.common.model.EntityBase
import org.hibernate.annotations.Type
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import java.math.BigDecimal
import java.time.Instant
import javax.money.MonetaryAmount
import javax.persistence.*

@Entity
@Table(name = "account")
class AccountEntity : EntityBase() {
    @Enumerated(EnumType.STRING)
    var accountType: AccountType = AccountType.WALLET

    @Column(name = "number")
    var accountNumber: String = ""

    var balance: BigDecimal = BigDecimal.ZERO

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var extraInfo: AccountInformation? = null

    @LastModifiedDate
    @Column(name = "last_update")
    var lastModified: Instant = Instant.EPOCH

    @Version
    var version: Int = 0

    fun transferTo(another: AccountEntity, amount: MonetaryAmount): TransactionEntity {
        val transferAmount = amount.number.numberValue(BigDecimal::class.java)

        balance -= transferAmount
        another.balance += transferAmount

        return TransactionEntity().also {
            it.amount = transferAmount
            it.currency = amount.currency.currencyCode
            it.fromAccount = requireNotNull(id)
            it.toAccount = requireNotNull(another.id)
            it.transactionTime = Instant.now()
            it.settleTime  = Instant.now()
        }
    }
}
