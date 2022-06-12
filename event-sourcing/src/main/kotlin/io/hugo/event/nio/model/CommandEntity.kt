package io.hugo.event.nio.model

import io.hugo.common.model.R2EntityBase
import io.r2dbc.postgresql.codec.Json
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("command")
class CommandEntity : R2EntityBase() {

    var commandType: String = ""

    var commandTime: Instant = Instant.EPOCH

    lateinit var commandData: Json
}
