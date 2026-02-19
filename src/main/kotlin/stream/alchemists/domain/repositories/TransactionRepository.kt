package stream.alchemists.domain.repositories

import stream.alchemists.domain.models.CreateTransactionRequest
import stream.alchemists.domain.models.Transaction
import java.util.*

interface TransactionRepository {
    suspend fun create(userId: UUID, request: CreateTransactionRequest): Transaction
    suspend fun findAllByUser(userId: UUID): List<Transaction>
    suspend fun findAllByAccount(accountId: UUID): List<Transaction>
    suspend fun findById(id: UUID): Transaction?
    suspend fun delete(id: UUID): Transaction?
}
