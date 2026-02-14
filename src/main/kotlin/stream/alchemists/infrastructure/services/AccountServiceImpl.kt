package stream.alchemists.infrastructure.services

import stream.alchemists.domain.exceptions.NotFoundException
import stream.alchemists.domain.models.Account
import stream.alchemists.domain.models.CreateAccountRequest
import stream.alchemists.domain.models.UpdateAccountRequest
import stream.alchemists.domain.repositories.AccountRepository
import stream.alchemists.domain.services.AccountService
import java.util.*

class AccountServiceImpl(
    private val accountRepository: AccountRepository
) : AccountService {
    override suspend fun create(userId: UUID, request: CreateAccountRequest): Account {
        return accountRepository.create(userId, request)
    }

    override suspend fun findAllByUser(userId: UUID): List<Account> {
        return accountRepository.findAllByUser(userId)
    }

    override suspend fun findById(userId: UUID, id: UUID): Account {
        val account = accountRepository.findById(id)
            ?: throw NotFoundException("account with $id not found")
        if (account.userId != userId.toString()) {
            throw NotFoundException("account with $id not found")
        }
        return account
    }

    override suspend fun update(userId: UUID, id: UUID, request: UpdateAccountRequest): Account {
        findById(userId, id)
        return accountRepository.update(id, request)
    }

    override suspend fun delete(userId: UUID, id: UUID) {
        findById(userId, id)
        accountRepository.delete(id)
    }
}
