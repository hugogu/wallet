package io.hugo.wallet.workflow

import io.hugo.wallet.model.TransactionEntity
import io.temporal.activity.ActivityOptions
import io.temporal.workflow.CompletablePromise
import io.temporal.workflow.Workflow
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*
import javax.money.MonetaryAmount

@Service
class WalletWorkflowImpl : WalletWorkflow {
    private lateinit var transactionPromise: CompletablePromise<TransactionEntity>

    private val walletActivity: WalletActivity by lazy {
        val defaultActivityOptions =
            ActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofDays(5))
                .build()

        Workflow.newActivityStub(WalletActivity::class.java, defaultActivityOptions)
    }

    override fun getTransactionEntity(): TransactionEntity {
        return transactionPromise.get()
    }

    override fun transfer(id: UUID, from: UUID, to: UUID, monetary: MonetaryAmount): TransactionEntity {
        transactionPromise = Workflow.newPromise()
        val transaction = walletActivity.processTransaction(id, from, to, monetary)
        transactionPromise.complete(transaction)
        walletActivity.broadcastBalanceUpdate(transaction.getBalanceActivities())

        return transaction
    }
}
