package io.hugo.wallet.stream

import io.hugo.common.serialization.SpringObjectMapperSupplier
import io.hugo.wallet.model.event.AccountBalanceActivity
import io.hugo.wallet.model.event.FraudAlert
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema
import org.apache.flink.connector.kafka.sink.KafkaSink
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer

class AccountBalanceActivityDeserializer :
    JsonDeserializer<AccountBalanceActivity>(SpringObjectMapperSupplier.INSTANCE)

class FraudAlertSerializer :
    JsonSerializer<FraudAlert>(SpringObjectMapperSupplier.INSTANCE)

@Configuration
class FraudDetectionJobConfig {
    @Bean
    fun streamEnvironment(): StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment()

    @Bean
    fun source(): KafkaSource<AccountBalanceActivity> {
        return KafkaSource.builder<AccountBalanceActivity>()
            .setBootstrapServers("kafka:9092")
            .setTopics("BalanceActivity")
            .setGroupId("FraudDetection")
            .setStartingOffsets(OffsetsInitializer.latest())
            .setDeserializer(KafkaRecordDeserializationSchema.valueOnly(AccountBalanceActivityDeserializer::class.java))
            .build()
    }

    @Bean
    fun sink(): KafkaSink<FraudAlert> =
        KafkaSink.builder<FraudAlert>()
            .setBootstrapServers("kafka:9092")
            .setRecordSerializer(KafkaRecordSerializationSchema.builder<FraudAlert>()
                .setTopic("FraudAlert")
                .setKafkaValueSerializer(FraudAlertSerializer::class.java)
                .build())
            .build()
}
