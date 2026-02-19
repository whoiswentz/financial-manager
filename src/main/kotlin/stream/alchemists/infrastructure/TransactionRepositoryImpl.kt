package stream.alchemists.infrastructure

import org.jetbrains.exposed.dao.id.EntityID
import stream.alchemists.db.*
import stream.alchemists.domain.exceptions.NotFoundException
import stream.alchemists.domain.models.CreateTransactionRequest
import stream.alchemists.domain.models.Transaction
import stream.alchemists.domain.repositories.TransactionRepository
import java.util.*

class TransactionRepositoryImpl : TransactionRepository {
    override suspend fun create(userId: UUID, request: CreateTransactionRequest): Transaction = dbQuery {
        TransactionEntity.new {
            amount = request.amount
            description = request.description
            type = request.type.name
            categoryId = request.categoryId?.let { EntityID(UUID.fromString(it), Categories) }
            accountId = EntityID(UUID.fromString(request.accountId), Accounts)
            this.userId = EntityID(userId, Users)
            createdAt = System.currentTimeMillis()
        }.let { TransactionEntity.toDomain(it) }
    }

    override suspend fun findAllByUser(userId: UUID): List<Transaction> = dbQuery {
        TransactionEntity.find { Transactions.userId eq userId }
            .map { TransactionEntity.toDomain(it) }
    }

    override suspend fun findAllByAccount(accountId: UUID): List<Transaction> = dbQuery {
        TransactionEntity.find { Transactions.accountId eq accountId }
            .map { TransactionEntity.toDomain(it) }
    }

    override suspend fun findById(id: UUID): Transaction? = dbQuery {
        TransactionEntity.findById(id)?.let { TransactionEntity.toDomain(it) }
    }

    override suspend fun delete(id: UUID): Transaction? = dbQuery {
        val entity = TransactionEntity.findById(id) ?: return@dbQuery null
        val transaction = TransactionEntity.toDomain(entity)
        entity.delete()
        transaction
    }
}
