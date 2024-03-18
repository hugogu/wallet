package io.hugo.wallet.workflow

import io.hugo.wallet.model.TransactionEntity
import io.temporal.workflow.QueryMethod
import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod
import java.util.*
import javax.money.MonetaryAmount

@WorkflowInterface
interface WalletWorkflow {
    @WorkflowMethod
    fun transfer(id: UUID, from: UUID, to: UUID, monetary: MonetaryAmount): TransactionEntity

    @QueryMethod
    fun getTransactionEntity(): TransactionEntity
}
