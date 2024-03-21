package io.hugo.wallet.stream

import io.hugo.wallet.model.event.AccountBalanceActivity
import io.hugo.wallet.model.event.FraudAlert
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.connector.kafka.sink.KafkaSink
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Service

@Service
class FraudDetectionJobExecutor(
    private val env: StreamExecutionEnvironment,
    private val source: KafkaSource<AccountBalanceActivity>,
    private val sink: KafkaSink<FraudAlert>
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        logger.info("Starting Fraud Detection Job")
        env.fromSource(source, WatermarkStrategy.noWatermarks(), "transactions")
            .keyBy(AccountBalanceActivity::accountId)
            .process(FraudDetector())
            .name("fraud-detector")
            .sinkTo(sink)
            .name("send-alerts")
        env.execute("Fraud Detection")
        logger.info("Started Fraud Detection Job")
    }

    private val logger = LoggerFactory.getLogger(FraudDetectionJobExecutor::class.java)
}
