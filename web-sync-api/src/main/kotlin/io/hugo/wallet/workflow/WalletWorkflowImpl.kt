package io.hugo.wallet.workflow

import io.hugo.wallet.model.TransactionEntity
import io.hugo.wallet.model.TransactionSpec
import io.temporal.activity.ActivityOptions
import io.temporal.workflow.Workflow
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class WalletWorkflowImpl : WalletWorkflow {
    private val walletActivity: WalletActivity by lazy {
        val defaultActivityOptions =
            ActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofDays(5))
                .build()

        Workflow.newActivityStub(WalletActivity::class.java, defaultActivityOptions)
    }

    override fun transfer(spec: TransactionSpec): TransactionEntity {
        val transaction = walletActivity.processTransaction(spec)
        walletActivity.broadcastBalanceUpdate(transaction.getBalanceActivities())

        return transaction
    }
}
