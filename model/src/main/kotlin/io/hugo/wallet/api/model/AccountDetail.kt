package io.hugo.wallet.api.model

import io.hugo.wallet.model.AccountType
import java.time.Instant
import java.util.UUID

data class AccountDetail(
    val id: UUID = UUID(0, 0),
    val accountNumber: String = "",
    val accountType: AccountType? = null,
    val createTime: Instant = Instant.EPOCH,
)
