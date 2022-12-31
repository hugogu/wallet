package io.hugo.event.model

object EventSourcingContext {
    private val currentCommand: ThreadLocal<ExecutableCommand> = ThreadLocal()

    fun getCurrentCommand(): ExecutableCommand? {
        return currentCommand.get()
    }

    fun setCurrentCommand(command: ExecutableCommand? = null) {
        currentCommand.set(command)
    }
}
