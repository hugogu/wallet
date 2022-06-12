package io.hugo.wallet.model

import io.r2dbc.postgresql.codec.Json
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import javax.money.MonetaryAmount

@Table("account")
class AccountEntity {
    @Id
    var id: UUID = UUID(0, 0)

    var accountType: AccountType = AccountType.WALLET

    @Column("number")
    var accountNumber: String = ""

    var balance: BigDecimal = BigDecimal.ZERO

    var extraInfo: Json? = null

    @CreatedDate
    var createTime: Instant = Instant.EPOCH

    @LastModifiedDate
    @Column("last_update")
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
            it.fromAccount = id
            it.toAccount = another.id
            it.transactionTime = Instant.now()
            it.settleTime  = Instant.now()
        }
    }
}
