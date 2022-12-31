package io.hugo.event.blocking.controller

import io.hugo.event.blocking.dal.CommandRepo
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class CommandController(
    private val commandRepo: CommandRepo,
) {
    @PostMapping(path = ["/operation/command/{id}"])
    fun replay(
        @PathVariable id: UUID,
        @RequestParam("local", defaultValue = "false", required = false)
        local: Boolean = false,
    ): Any {
        val command = commandRepo.getReferenceById(id)
        val result = command.execute()
        return result
    }

    private val logger = LoggerFactory.getLogger(CommandController::class.java)
}
