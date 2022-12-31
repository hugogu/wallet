package io.hugo.wallet.service

import io.hugo.common.messaging.HttpSourcedMessage
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class CallbackProcessor(
) {
    @KafkaListener(topics = ["Callbacks"], groupId = "callback-persist")
    fun persistCallback(message: HttpSourcedMessage) {
        logger.info("Received $message")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CallbackProcessor::class.java)
    }
}
