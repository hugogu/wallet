package io.hugo.event.blocking.model

import com.fasterxml.jackson.databind.JsonNode
import io.hugo.common.model.EntityBase
import org.hibernate.annotations.Type
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class CommandEntity : EntityBase(){
    var commandType: String = ""

    var commandTime: Instant = Instant.EPOCH

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    lateinit var commandData: JsonNode
}
