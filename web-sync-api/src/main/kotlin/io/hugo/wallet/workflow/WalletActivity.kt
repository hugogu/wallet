package io.hugo.wallet.workflow

import io.hugo.wallet.model.TransactionEntity
import io.hugo.wallet.model.event.AccountBalanceActivity
import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod
import java.util.*
import javax.money.MonetaryAmount
import kotlin.collections.Collection

@ActivityInterface
interface WalletActivity {
    @ActivityMethod
    fun broadcastBalanceUpdate(balanceActivities: Collection<AccountBalanceActivity>)

    @ActivityMethod
    fun processTransaction(transactionId: UUID, from: UUID, to: UUID, amount: MonetaryAmount): TransactionEntity
}
