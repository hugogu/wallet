package io.hugo.wallet.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.yahoo.elide.annotation.Include
import io.hugo.common.model.EntityBase
import io.hugo.wallet.model.event.AccountBalanceActivity
import org.javamoney.moneta.FastMoney
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import javax.money.MonetaryAmount
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Include
@Entity
@Table(name = "transaction")
class TransactionEntity : EntityBase() {
    var fromAccount: UUID = UUID(0, 0)

    var toAccount: UUID = UUID(0, 0)

    var currency: String = ""

    var amount: BigDecimal = BigDecimal.ZERO

    var transactionTime: Instant = Instant.EPOCH

    var settleTime: Instant? = null

    @LastModifiedDate
    @Column(name = "last_update")
    var lastModified: Instant = Instant.EPOCH

    @Version
    var version: Int = 0

    @JsonIgnore
    fun getTransactionAmount(): MonetaryAmount = FastMoney.of(amount, currency)

    @JsonIgnore
    fun getBalanceActivities(): Pair<AccountBalanceActivity, AccountBalanceActivity> {
        return AccountBalanceActivity(id!!, fromAccount, getTransactionAmount().negate(), transactionTime) to
            AccountBalanceActivity(id!!, toAccount, getTransactionAmount(), transactionTime)
    }
}
