package io.hugo.event.model.http.config

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.module.SimpleModule
import io.hugo.event.model.ExecutableCommand
import io.hugo.event.model.http.HttpRequestCommand

class EventSourcingJacksonModule : SimpleModule("EventSourcing-Http",
    Version(0, 0, 1, null, "io.hugo", "common-jackson-http")
) {
    init {
        setMixInAnnotation(ExecutableCommand::class.java, HttpCommandMixin::class.java)
    }

    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
    )
    @JsonSubTypes(
        JsonSubTypes.Type(value = HttpRequestCommand::class, name = HttpRequestCommand.TYPE),
    )
    class HttpCommandMixin
}
