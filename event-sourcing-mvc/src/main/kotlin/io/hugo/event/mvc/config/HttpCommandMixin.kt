package io.hugo.event.mvc.config

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.hugo.event.mvc.HttpRequestCommand

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = HttpRequestCommand::class, name = HttpRequestCommand.TYPE),
)
class HttpCommandMixin
