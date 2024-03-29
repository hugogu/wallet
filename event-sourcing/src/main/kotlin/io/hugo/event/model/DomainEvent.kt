package io.hugo.event.model

import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * A marker interface represents a domain event data.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
interface DomainEvent
