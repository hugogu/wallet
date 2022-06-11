package io.hugo.wallet.dal

import io.hugo.wallet.model.AccountEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AccountRepo : ReactiveCrudRepository<AccountEntity, UUID> {
}
