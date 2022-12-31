package io.hugo.event.config

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.module.SimpleModule
import io.hugo.event.blocking.mvc.HttpCommandMixin
import io.hugo.event.model.ExecutableCommand

class EventSourcingJacksonModule : SimpleModule("EventSourcing",
    Version(0, 0, 1, null, null, null)
) {
    init {
        setMixInAnnotation(ExecutableCommand::class.java, HttpCommandMixin::class.java)
    }
}