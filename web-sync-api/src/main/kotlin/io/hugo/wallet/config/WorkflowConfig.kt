package io.hugo.wallet.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.hugo.wallet.workflow.WalletActivity
import io.hugo.wallet.workflow.WalletWorkflowImpl
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowClientOptions
import io.temporal.client.WorkflowOptions
import io.temporal.common.converter.DefaultDataConverter
import io.temporal.common.converter.JacksonJsonPayloadConverter
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.worker.Worker
import io.temporal.worker.WorkerFactory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.SmartInitializingSingleton
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WorkflowConfig {
    @Bean
    fun workflowClient(objectMapper: ObjectMapper): WorkflowClient {
        val service = WorkflowServiceStubs.newLocalServiceStubs()
        val bodyConverter = JacksonJsonPayloadConverter(objectMapper)
        val clientOptions = WorkflowClientOptions.newBuilder()
            .setDataConverter(DefaultDataConverter.newDefaultInstance().withPayloadConverterOverrides(bodyConverter))
            .build()

        return WorkflowClient.newInstance(service, clientOptions)
    }

    @Bean
    fun workflowOptions(): WorkflowOptions {
        return WorkflowOptions.newBuilder()
            .setTaskQueue(WALLET_TRANSFER_TASK_QUEUE)
            .build()
    }

    @Bean
    fun workerFactory(client: WorkflowClient): WorkerFactory = WorkerFactory.newInstance(client)

    @Bean
    fun walletTransferWorker(
        workerFactory: WorkerFactory,
        workflowClient: WorkflowClient,
        walletActivity: WalletActivity,
    ): Worker {
        return workerFactory.newWorker(WALLET_TRANSFER_TASK_QUEUE).apply {
            registerActivitiesImplementations(walletActivity)
            registerWorkflowImplementationTypes(WalletWorkflowImpl::class.java)
        }
    }

    @Bean
    fun workFactoryStarter(factory: WorkerFactory) = SmartInitializingSingleton {
        factory.start()
        logger.info("Temporal work started.")
    }

    companion object {
        const val WALLET_TRANSFER_TASK_QUEUE = "wallet-transfer-task"
        private val logger = LoggerFactory.getLogger(WorkflowConfig::class.java)
    }
}
