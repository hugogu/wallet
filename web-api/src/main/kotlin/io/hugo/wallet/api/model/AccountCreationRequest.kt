package io.hugo.wallet.api.model

import io.hugo.wallet.model.AccountEntity
import io.hugo.wallet.model.AccountType
import java.util.UUID

data class AccountCreationRequest(
    val accountNumber: String = "",
    val type: AccountType = AccountType.WALLET,
) {
    fun toEntity(id: UUID) = AccountEntity().also {
        it.id = id
        it.accountType = type
        it.accountNumber = accountNumber
    }
}
