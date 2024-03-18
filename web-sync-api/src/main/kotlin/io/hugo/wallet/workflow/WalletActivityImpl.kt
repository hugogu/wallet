package io.hugo.wallet.workflow

import io.hugo.wallet.config.WalletApplicationConfig
import io.hugo.wallet.dal.AccountSyncRepo
import io.hugo.wallet.dal.TransactionSyncRepo
import io.hugo.wallet.model.TransactionEntity
import io.hugo.wallet.model.event.AccountBalanceActivity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.money.MonetaryAmount

@Service
class WalletActivityImpl(
    private val accountRepo: AccountSyncRepo,
    private val transactionRepo: TransactionSyncRepo,
) : WalletActivity {
    override fun broadcastBalanceUpdate(balanceActivities: Collection<AccountBalanceActivity>) {
        balanceActivities.forEach(WalletApplicationConfig.BalanceSink::tryEmitNext)
    }

    @Transactional
    override fun processTransaction(
        transactionId: UUID,
        from: UUID,
        to: UUID,
        amount: MonetaryAmount
    ): TransactionEntity {
        val fromAccount = accountRepo.getWithLockById(from)
        val toAccount = accountRepo.getWithLockById(to)
        val transaction = fromAccount.transferTo(toAccount, amount).also {
            it.setId(transactionId)
        }

        return transactionRepo.save(transaction)
    }
}