package io.hugo.event.kafka

import io.hugo.event.blocking.dal.EventRepo
import org.apache.kafka.clients.producer.ProducerInterceptor
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.springframework.context.ApplicationContext
import java.lang.Exception

class EventSourcingProducerInterceptor<K, V> :  ProducerInterceptor<K, V> {
    private var configs: Map<String, Any?> = emptyMap()

    private fun getEventRepo(): EventRepo {
        return requireNotNull(context).getBean(EventRepo::class.java)
    }

    private fun getEventConverter(): RecordDataEventConverter<K, V> {
        return requireNotNull(context).getBean(RecordDataEventConverter::class.java) as RecordDataEventConverter<K, V>
    }

    override fun onSend(record: ProducerRecord<K, V>): ProducerRecord<K, V> {
        val entity = getEventConverter().convert(record, configs)
        getEventRepo().saveAndFlush(entity)

        return record
    }

    override fun configure(configs: MutableMap<String, *>?) {
        this.configs = configs.orEmpty().toMap()
    }

    override fun onAcknowledgement(metadata: RecordMetadata?, exception: Exception?) {
    }

    override fun close() {
        getEventRepo().flush()
    }

    companion object {
        internal var context: ApplicationContext? = null
    }
}
