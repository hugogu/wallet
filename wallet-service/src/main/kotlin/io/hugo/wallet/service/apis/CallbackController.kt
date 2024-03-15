package io.hugo.wallet.service.apis

import io.hugo.common.messaging.HttpSourcedMessage
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange

/**
 * Captures incoming HTTP callbacks and forwards them to Kafka for further processing.
 * This is a simplified implementation of [Kafka REST proxy](https://github.com/confluentinc/kafka-rest).
 */
@RestController
class CallbackController(
    private val eventPublisher: ApplicationEventPublisher
) {
    @PostMapping("/callback")
    fun onCallback(
        @RequestBody body: String,
        @RequestHeader headers: Map<String, String>,
        exchange: ServerWebExchange,
    ) {
        val message = HttpSourcedMessage(headers, exchange.request.uri.toString(), body)
        eventPublisher.publishEvent(message)
        logger.info("Captured callback message $message")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CallbackController::class.java)
    }
}
