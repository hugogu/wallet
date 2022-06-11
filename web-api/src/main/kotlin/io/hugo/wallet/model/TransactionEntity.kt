package io.hugo.wallet.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Table("transaction")
class TransactionEntity {
    @Id
    var id: UUID = UUID(0, 0)

    var fromAccount: UUID = UUID(0, 0)

    var toAccount: UUID = UUID(0, 0)

    var currency: String = ""

    var amount: BigDecimal = BigDecimal.ZERO

    var transactionTime: Instant = Instant.EPOCH

    var settleTime: Instant = Instant.EPOCH

    @CreatedDate
    var createTime: Instant = Instant.EPOCH

    @LastModifiedDate
    @Column("last_update")
    var lastModified: Instant = Instant.EPOCH

    @Version
    var version: Int = 0
}
