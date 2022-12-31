package io.hugo.wallet.service.apis

import io.hugo.common.messaging.HttpSourcedMessage
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaOperations
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.ServletWebRequest

@RestController
class CallbackController(
    private val kafkaTemplate: KafkaOperations<String, Any>
) {
    @PostMapping("/callback")
    fun onCallback(
        @RequestBody body: String,
        @RequestHeader headers: Map<String, String>,
        request: ServletWebRequest,
    ) {
        val message = HttpSourcedMessage(headers, request.toString(), body)
        kafkaTemplate.send("Callbacks", message).completable().whenComplete { result, error ->
            if (error != null) {
                logger.error("Failed to capture message $body.", error)
            } else {
                logger.info("Captured callback message $message to ${result.recordMetadata}")
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CallbackController::class.java)
    }
}
