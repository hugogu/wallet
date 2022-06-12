package io.hugo.wallet.api

import io.hugo.wallet.api.model.*
import io.hugo.wallet.dal.AccountRepo
import io.hugo.wallet.dal.TransactionRepo
import io.hugo.wallet.model.AccountEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.UUID

@Validated
@RestController("/wallet")
class WalletController(
    private val accountRepo: AccountRepo,
    private val transactionRepo: TransactionRepo,
) {
    @PostMapping("/account")
    fun createAccount(
        @RequestBody request: AccountCreationRequest,
        @RequestHeader("X-Request-ID") requestId: UUID,
    ): Mono<ResourceIdentity> {
        return accountRepo.save(request.toEntity(requestId)).map {
            ResourceIdentity(it.id)
        }
    }

    @GetMapping("/account/{id}")
    fun queryAccountDetail(@PathVariable id: UUID): Mono<AccountDetail> {
        return accountRepo.findById(id).map {
            AccountDetail(it.id, it.accountNumber, it.accountType, it.createTime)
        }
    }

    @PostMapping("/transfer")
    fun transfer(
        @RequestBody request: TransferRequest,
        @RequestHeader("X-Request-ID", required = false) requestId: UUID? = null,
    ): Mono<ResourceIdentity> {
        val from = accountRepo.findById(request.from)
        val to = accountRepo.findById(request.to)
        return from.zipWith(to).flatMapMany { pair ->
            val transaction = pair.t1.transferTo(pair.t2, request.monetary).also {
                it.id = requestId ?: UUID.randomUUID()
            }

            Flux.merge(
                accountRepo.save(pair.t1).map { ResourceIdentity(it.id) },
                accountRepo.save(pair.t2).map { ResourceIdentity(it.id) },
                transactionRepo.save(transaction).map { ResourceIdentity(it.id, ResourceType.TRANSACTION) }
            )
        }.filter { it.resourceType == ResourceType.TRANSACTION }.toMono()
    }

    companion object {
        fun AccountCreationRequest.toEntity(id: UUID) = AccountEntity().also {
            it.id = id
            it.accountType = type
            it.accountNumber = accountNumber
        }
    }
}
