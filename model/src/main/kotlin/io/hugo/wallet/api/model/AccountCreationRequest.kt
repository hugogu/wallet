package io.hugo.wallet.api.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.hugo.wallet.model.AccountType

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
data class AccountCreationRequest(
    val accountNumber: String = "",
    val type: AccountType = AccountType.WALLET,
)
