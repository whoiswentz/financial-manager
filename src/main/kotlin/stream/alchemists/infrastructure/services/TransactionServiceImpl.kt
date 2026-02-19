package stream.alchemists.infrastructure.services

import stream.alchemists.domain.exceptions.NotFoundException
import stream.alchemists.domain.models.CreateTransactionRequest
import stream.alchemists.domain.models.Transaction
import stream.alchemists.domain.models.TransactionType
import stream.alchemists.domain.repositories.AccountRepository
import stream.alchemists.domain.repositories.TransactionRepository
import stream.alchemists.domain.services.TransactionService
import java.util.*

class TransactionServiceImpl(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
) : TransactionService {
    override suspend fun create(userId: UUID, request: CreateTransactionRequest): Transaction {
        val accountId = UUID.fromString(request.accountId)
        val account = accountRepository.findById(accountId)
            ?: throw NotFoundException("account with ${request.accountId} not found")
        if (account.userId != userId.toString()) {
            throw NotFoundException("account with ${request.accountId} not found")
        }

        val transaction = transactionRepository.create(userId, request)

        val delta = when (request.type) {
            TransactionType.INCOME -> request.amount
            TransactionType.EXPENSE -> -request.amount
        }
        accountRepository.updateBalance(accountId, delta)

        return transaction
    }

    override suspend fun findAllByUser(userId: UUID): List<Transaction> {
        return transactionRepository.findAllByUser(userId)
    }

    override suspend fun findAllByAccount(userId: UUID, accountId: UUID): List<Transaction> {
        val account = accountRepository.findById(accountId)
            ?: throw NotFoundException("account with $accountId not found")
        if (account.userId != userId.toString()) {
            throw NotFoundException("account with $accountId not found")
        }
        return transactionRepository.findAllByAccount(accountId)
    }

    override suspend fun findById(userId: UUID, id: UUID): Transaction {
        val transaction = transactionRepository.findById(id)
            ?: throw NotFoundException("transaction with $id not found")
        if (transaction.userId != userId.toString()) {
            throw NotFoundException("transaction with $id not found")
        }
        return transaction
    }

    override suspend fun delete(userId: UUID, id: UUID) {
        val transaction = findById(userId, id)
        transactionRepository.delete(id)

        val delta = when (TransactionType.valueOf(transaction.type.name)) {
            TransactionType.INCOME -> -transaction.amount
            TransactionType.EXPENSE -> transaction.amount
        }
        accountRepository.updateBalance(UUID.fromString(transaction.accountId), delta)
    }
}
