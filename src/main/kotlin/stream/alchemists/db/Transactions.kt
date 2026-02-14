package stream.alchemists.db

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import stream.alchemists.domain.models.Transaction
import stream.alchemists.domain.models.TransactionType
import java.util.*

object Transactions : UUIDTable("transactions") {
    val amount = double("amount")
    val description = varchar("description", 255).nullable()
    val type = varchar("type", 50)
    val categoryId = reference("category_id", Categories).nullable()
    val accountId = reference("account_id", Accounts)
    val userId = reference("user_id", Users)
    val createdAt = long("created_at")
}

class TransactionEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    var amount by Transactions.amount
    var description by Transactions.description
    var type by Transactions.type
    var categoryId by Transactions.categoryId
    var accountId by Transactions.accountId
    var userId by Transactions.userId
    var createdAt by Transactions.createdAt

    companion object : EntityClass<UUID, TransactionEntity>(Transactions) {
        fun toDomain(entity: TransactionEntity): Transaction = Transaction(
            id = entity.id.toString(),
            amount = entity.amount,
            description = entity.description,
            type = TransactionType.valueOf(entity.type),
            categoryId = entity.categoryId?.toString(),
            accountId = entity.accountId.toString(),
            userId = entity.userId.toString(),
            createdAt = entity.createdAt,
        )
    }
}
