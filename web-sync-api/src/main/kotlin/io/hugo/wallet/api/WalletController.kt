package io.hugo.wallet.api

import io.hugo.wallet.api.model.*
import io.hugo.wallet.dal.AccountSyncRepo
import io.hugo.wallet.dal.TransactionSyncRepo
import io.hugo.wallet.model.AccountEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Validated
@RestController("/wallet")
class WalletController(
    private val accountRepo: AccountSyncRepo,
    private val transactionRepo: TransactionSyncRepo,
) {
    @PostMapping("/account")
    @Transactional
    fun createAccount(
        @RequestBody request: AccountCreationRequest,
        @RequestHeader("X-Request-ID") requestId: UUID,
    ): ResourceIdentity {
        val entity = accountRepo.save(request.toEntity(requestId))
        return ResourceIdentity(entity.id!!)
    }

    @GetMapping("/account/{id}")
    fun queryAccountDetail(@PathVariable id: UUID): AccountDetail {
        val entity = accountRepo.getReferenceById(id)
        return AccountDetail(entity.id!!, entity.accountNumber, entity.accountType, entity.createTime)
    }

    @PostMapping("/transfer")
    @Transactional
    fun transferSync(
        @RequestBody request: TransferRequest,
        @RequestHeader("X-Request-ID", required = false) requestId: UUID? = null,
    ) : ResourceIdentity {
        val from = accountRepo.getReferenceById(request.from)
        val to = accountRepo.getReferenceById(request.to)
        val transaction = from.transferTo(to, request.monetary).also {
            it.setId(requestId ?: UUID.randomUUID())
            it.new = true
        }

        transactionRepo.save(transaction)

        return ResourceIdentity(transaction.id!!, ResourceType.TRANSACTION)
    }

    companion object {
        fun AccountCreationRequest.toEntity(id: UUID) = AccountEntity().also {
            it.setId(id)
            it.accountType = type
            it.accountNumber = accountNumber
        }
    }
}
