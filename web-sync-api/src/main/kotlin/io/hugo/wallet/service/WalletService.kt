package io.hugo.wallet.service

import io.hugo.wallet.model.TransactionEntity
import io.hugo.wallet.workflow.WalletWorkflow
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
import org.springframework.stereotype.Service
import java.util.*
import javax.money.MonetaryAmount

@Service
class WalletService(
    private val client: WorkflowClient,
    private val options: WorkflowOptions,
) {
    fun transfer(id: UUID, from: UUID, to: UUID, monetary: MonetaryAmount): TransactionEntity {
        val workflow = client.newWorkflowStub(WalletWorkflow::class.java, options)
        val result = WorkflowClient.execute(workflow::transfer, id, from, to, monetary)
        // TODO: return the result prior to the completion of the workflow

        return result.get()
    }
}
