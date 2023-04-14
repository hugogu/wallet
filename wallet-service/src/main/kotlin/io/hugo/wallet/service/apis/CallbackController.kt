package io.hugo.wallet.service.apis

import io.hugo.common.messaging.HttpSourcedMessage
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.ServletWebRequest

@RestController
class CallbackController(
    private val eventPublisher: ApplicationEventPublisher
) {
    @PostMapping("/callback")
    fun onCallback(
        @RequestBody body: String,
        @RequestHeader headers: Map<String, String>,
        request: ServletWebRequest,
    ) {
        val message = HttpSourcedMessage(headers, request.toString(), body)
        eventPublisher.publishEvent(message)
        logger.info("Captured callback message $message")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CallbackController::class.java)
    }
}
