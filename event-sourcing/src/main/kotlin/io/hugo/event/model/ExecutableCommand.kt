package io.hugo.event.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.UUID

interface ExecutableCommand {
    @get:JsonIgnore
    val id: UUID

    fun execute(options: CommandOptions): Any
}
