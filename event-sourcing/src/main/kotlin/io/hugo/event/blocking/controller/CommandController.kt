package io.hugo.event.blocking.controller

import io.hugo.event.blocking.dal.CommandRepo
import io.hugo.event.model.CommandOptions
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class CommandController(
    private val commandRepo: CommandRepo,
) {
    @PostMapping(path = ["/operation/command/{id}"])
    fun replay(
        @PathVariable id: UUID,
        @RequestBody(required = false) options: CommandOptions? = null,
    ): Any {
        val entity = commandRepo.getReferenceById(id)
        return try {
            val result = entity.commandData.execute(options ?: CommandOptions())
            logger.info("Executed ${entity.commandData} and got $result")
            result
        } catch (ex: Exception) {
            logger.info("Executed ${entity.commandData} and failed with ${ex.message}", ex)
        }
    }

    private val logger = LoggerFactory.getLogger(CommandController::class.java)
}
