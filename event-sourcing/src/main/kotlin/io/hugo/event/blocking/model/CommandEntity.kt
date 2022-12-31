package io.hugo.event.blocking.model

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.hugo.common.model.EntityBase
import io.hugo.event.model.ExecutableCommand
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.time.Instant
import javax.persistence.Column
import javax.persistence.DiscriminatorColumn
import javax.persistence.DiscriminatorType
import javax.persistence.Entity
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.Table

@Entity
@Table(name = "command")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name = "command_type",
    discriminatorType = DiscriminatorType.STRING,
    length = 32,
)
@TypeDefs(TypeDef(name = "jsonb", typeClass = JsonBinaryType::class))
abstract class CommandEntity<T: ExecutableCommand> : EntityBase() {
    var commandTime: Instant = Instant.EPOCH

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    open lateinit var commandData: T
}
