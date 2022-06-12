package io.hugo.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.annotation.Transient
import java.time.Instant
import java.util.UUID

abstract class R2EntityBase : Persistable<UUID> {
    @Id
    private var id: UUID = UUID(0, 0)

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
