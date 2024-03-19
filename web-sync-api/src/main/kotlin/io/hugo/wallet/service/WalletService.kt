package io.hugo.wallet.service

import io.hugo.wallet.model.TransactionSpec
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
    fun transfer(id: UUID, from: UUID, to: UUID, monetary: MonetaryAmount): TransactionSpec {
        val workflow = client.newWorkflowStub(WalletWorkflow::class.java, options)
        val spec = TransactionSpec(id, from, to, monetary)
        WorkflowClient.start(workflow::transfer, spec)

        return spec
    }
}
