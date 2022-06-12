package io.hugo.wallet.api.model

import io.hugo.wallet.model.AccountType

data class AccountCreationRequest(
    val accountNumber: String = "",
    val type: AccountType = AccountType.WALLET,
)
