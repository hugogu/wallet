package io.hugo.wallet.workflow

import io.hugo.wallet.model.TransactionEntity
import io.hugo.wallet.model.TransactionSpec
import io.hugo.wallet.model.event.AccountBalanceActivity
import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod

@ActivityInterface
interface WalletActivity {
    @ActivityMethod
    fun broadcastBalanceUpdate(balanceActivities: Collection<AccountBalanceActivity>)

    @ActivityMethod
    fun processTransaction(spec: TransactionSpec): TransactionEntity
}
