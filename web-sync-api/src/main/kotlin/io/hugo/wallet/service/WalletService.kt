package io.hugo.wallet.service

import io.hugo.wallet.dal.AccountSyncRepo
import io.hugo.wallet.dal.TransactionSyncRepo
import io.hugo.wallet.model.TransactionEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.money.MonetaryAmount

@Service
class WalletService(
    private val accountRepo: AccountSyncRepo,
    private val transactionRepo: TransactionSyncRepo,
) {
    @Transactional
    fun transfer(id: UUID, from: UUID, to: UUID, monetary: MonetaryAmount): TransactionEntity {
        val fromAccount = accountRepo.getWithLockById(from)
        val toAccount = accountRepo.getWithLockById(to)
        val transaction = fromAccount.transferTo(toAccount, monetary).also {
            it.setId(id)
        }

        return transactionRepo.save(transaction)
    }
}
