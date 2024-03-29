package io.hugo.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Type
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID
import javax.persistence.EntityListeners
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.Transient

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class EntityBase : Persistable<UUID> {
    @Id
    @Type(type = "pg-uuid")
    private var id: UUID? = null

    @CreatedDate
    var createTime: Instant = Instant.EPOCH

    @Transient
    var new: Boolean = false

    override fun getId(): UUID? {
        return id
    }

    fun setId(id: UUID) {
        this.id = id
    }

    @JsonIgnore
    override fun isNew(): Boolean {
        return new
    }
}
