package io.hugo.event.blocking.controller

import io.hugo.event.blocking.dal.EventRepo
import io.hugo.event.model.command.CommandOptions
import io.hugo.event.model.event.EventOptions
import io.hugo.event.model.event.EventPublishTarget
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class EventController(
    private val eventRepo: EventRepo,
    private val eventPublisher: ApplicationEventPublisher,
) {
    @PostMapping(path = ["/operation/event/{id}"])
    fun replay(
        @PathVariable id: UUID,
        @RequestBody(required = false) options: EventOptions = EventOptions(),
    ): Any {
        val entity = eventRepo.getReferenceById(id)
        return try {
            when (options.mode) {
                EventPublishTarget.APP_EVENT -> eventPublisher.publishEvent(entity.getEvent())
                EventPublishTarget.KAFKA -> TODO("to support")
            }
            entity
        } catch (ex: Exception) {
            logger.info("Replay of ${entity.eventData} failed with ${ex.message}", ex)
        }
    }

    private val logger = LoggerFactory.getLogger(EventController::class.java)
}
