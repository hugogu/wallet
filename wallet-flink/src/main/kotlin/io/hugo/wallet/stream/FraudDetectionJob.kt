@file:JvmName("FraudDetection")

package io.hugo.wallet.stream

import io.hugo.common.serialization.SpringObjectMapperSupplier
import io.hugo.wallet.model.event.AccountBalanceActivity
import io.hugo.wallet.model.event.FraudAlert
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema
import org.apache.flink.connector.kafka.sink.KafkaSink
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer

class AccountBalanceActivityDeserializer : JsonDeserializer<AccountBalanceActivity>(SpringObjectMapperSupplier.INSTANCE) {
}

class FraudAlertSerializer : JsonSerializer<FraudAlert>(SpringObjectMapperSupplier.INSTANCE) {
}

fun main(args: Array<String>) {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment()

    val source = KafkaSource.builder<AccountBalanceActivity>()
        .setBootstrapServers("kafka:9092")
        .setTopics("BalanceActivity")
        .setGroupId("FraudDetection")
        .setStartingOffsets(OffsetsInitializer.earliest())
        .setDeserializer(KafkaRecordDeserializationSchema.valueOnly(AccountBalanceActivityDeserializer::class.java))
        .build()

    val transactions: DataStream<AccountBalanceActivity> = env
        .fromSource(source, WatermarkStrategy.noWatermarks(), "transactions")

    val alerts = transactions
        .keyBy(AccountBalanceActivity::accountId)
        .process(FraudDetector())
        .name("fraud-detector")

    val sink = KafkaSink.builder<FraudAlert>()
        .setBootstrapServers("kafka:9092")
        .setRecordSerializer(KafkaRecordSerializationSchema.builder<FraudAlert>()
            .setTopic("FraudAlert")
            .setKafkaValueSerializer(FraudAlertSerializer::class.java)
            .build())
        .build()

    alerts.sinkTo(sink).name("send-alerts")

    env.execute("Fraud Detection")
}
