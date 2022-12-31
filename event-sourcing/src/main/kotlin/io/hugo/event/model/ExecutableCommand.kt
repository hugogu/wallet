package io.hugo.event.model

interface ExecutableCommand {
    fun execute(options: CommandOptions): Any
}
