package io.hugo.wallet.dal

import io.hugo.wallet.model.TransactionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TransactionSyncRepo : JpaRepository<TransactionEntity, UUID> {
}
