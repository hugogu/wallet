package io.hugo.wallet.model

import io.hugo.common.model.EntityBase
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "transaction")
class TransactionEntity : EntityBase() {
    var fromAccount: UUID = UUID(0, 0)

    var toAccount: UUID = UUID(0, 0)

    var currency: String = ""

    var amount: BigDecimal = BigDecimal.ZERO

    var transactionTime: Instant = Instant.EPOCH

    var settleTime: Instant = Instant.EPOCH

    @LastModifiedDate
    @Column(name = "last_update")
    var lastModified: Instant = Instant.EPOCH

    @Version
    var version: Int = 0
}
