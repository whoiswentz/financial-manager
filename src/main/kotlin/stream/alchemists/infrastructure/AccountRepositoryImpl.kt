package stream.alchemists.infrastructure

import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.update
import stream.alchemists.db.AccountEntity
import stream.alchemists.db.Accounts
import stream.alchemists.db.dbQuery
import stream.alchemists.domain.exceptions.NotFoundException
import stream.alchemists.domain.models.Account
import stream.alchemists.domain.models.CreateAccountRequest
import stream.alchemists.domain.models.UpdateAccountRequest
import stream.alchemists.domain.repositories.AccountRepository
import java.util.*

class AccountRepositoryImpl : AccountRepository {
    override suspend fun create(userId: UUID, request: CreateAccountRequest): Account = dbQuery {
        AccountEntity.new {
            name = request.name
            type = request.type.name
            balance = 0.0
            this.userId = org.jetbrains.exposed.dao.id.EntityID(userId, stream.alchemists.db.Users)
        }.let { AccountEntity.toDomain(it) }
    }

    override suspend fun findAllByUser(userId: UUID): List<Account> = dbQuery {
        AccountEntity.find { Accounts.userId eq userId }
            .map { AccountEntity.toDomain(it) }
    }

    override suspend fun findById(id: UUID): Account? = dbQuery {
        AccountEntity.findById(id)?.let { AccountEntity.toDomain(it) }
    }

    override suspend fun update(id: UUID, request: UpdateAccountRequest): Account = dbQuery {
        val account = AccountEntity.findById(id)
            ?: throw NotFoundException("account with $id not found")

        account.name = request.name
        account.type = request.type.name

        AccountEntity.toDomain(account)
    }

    override suspend fun delete(id: UUID) = dbQuery {
        val account = AccountEntity.findById(id)
            ?: throw NotFoundException("account with $id not found")
        account.delete()
    }

    override suspend fun updateBalance(id: UUID, delta: Double) = dbQuery {
        Accounts.update({ Accounts.id eq id }) {
            it[balance] = balance + delta
        }
        Unit
    }
}
