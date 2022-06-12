package io.hugo.wallet.dal

import io.hugo.wallet.model.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AccountSyncRepo : JpaRepository<AccountEntity, UUID> {
}
