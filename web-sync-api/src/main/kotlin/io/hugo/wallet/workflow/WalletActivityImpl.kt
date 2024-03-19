package io.hugo.wallet.workflow

import io.hugo.wallet.config.WalletApplicationConfig
import io.hugo.wallet.dal.AccountSyncRepo
import io.hugo.wallet.dal.TransactionSyncRepo
import io.hugo.wallet.model.TransactionEntity
import io.hugo.wallet.model.TransactionSpec
import io.hugo.wallet.model.event.AccountBalanceActivity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WalletActivityImpl(
    private val accountRepo: AccountSyncRepo,
    private val transactionRepo: TransactionSyncRepo,
) : WalletActivity {
    override fun broadcastBalanceUpdate(balanceActivities: Collection<AccountBalanceActivity>) {
        balanceActivities.forEach(WalletApplicationConfig.BalanceSink::tryEmitNext)
    }

    @Transactional
    override fun processTransaction(spec: TransactionSpec): TransactionEntity {
        val fromAccount = accountRepo.getWithLockById(spec.fromAccount)
        val toAccount = accountRepo.getWithLockById(spec.toAccount)
        val transaction = fromAccount.transferTo(toAccount, spec.amount).also {
            it.setId(spec.id)
        }

        return transactionRepo.save(transaction)
    }
}
