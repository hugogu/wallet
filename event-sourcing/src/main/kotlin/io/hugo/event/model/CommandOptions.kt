package io.hugo.event.model

data class CommandOptions(
    /**
     * By default, the original requestId, if available, will be used in retrying a command.
     * Set this to `true` to generate new IDs.
     */
    val renewRequestIds: Boolean = false,

    val randomSeed: String = "",
)
