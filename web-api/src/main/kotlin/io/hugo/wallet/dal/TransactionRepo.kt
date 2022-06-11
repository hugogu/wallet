package io.hugo.wallet.dal

import io.hugo.wallet.model.TransactionEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TransactionRepo : ReactiveCrudRepository<TransactionEntity, UUID> {
}
