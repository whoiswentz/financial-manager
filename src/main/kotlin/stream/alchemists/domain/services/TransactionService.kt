package stream.alchemists.domain.services

import stream.alchemists.domain.models.CreateTransactionRequest
import stream.alchemists.domain.models.Transaction
import java.util.*

interface TransactionService {
    suspend fun create(userId: UUID, request: CreateTransactionRequest): Transaction
    suspend fun findAllByUser(userId: UUID): List<Transaction>
    suspend fun findAllByAccount(userId: UUID, accountId: UUID): List<Transaction>
    suspend fun findById(userId: UUID, id: UUID): Transaction
    suspend fun delete(userId: UUID, id: UUID)
}
