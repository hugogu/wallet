package io.hugo.event.model.internal

import org.springframework.context.ApplicationEvent

data class ReplayCommand<T: Any>(
    val entity: T,
    val option: ReplayOptions = ReplayOptions(),
) : ApplicationEvent(entity)
