package io.hugo.wallet.workflow

import io.hugo.wallet.model.TransactionEntity
import io.hugo.wallet.model.TransactionSpec
import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

@WorkflowInterface
interface WalletWorkflow {
    @WorkflowMethod
    fun transfer(spec: TransactionSpec): TransactionEntity
}
