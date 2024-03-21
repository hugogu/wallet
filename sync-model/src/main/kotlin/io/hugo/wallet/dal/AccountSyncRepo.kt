package io.hugo.wallet.dal

import io.hugo.wallet.model.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AccountSyncRepo : JpaRepository<AccountEntity, UUID> {
    @Query("SELECT * FROM account a WHERE a.id = :id FOR UPDATE", nativeQuery = true)
    fun getWithLockById(id: UUID): AccountEntity
}
