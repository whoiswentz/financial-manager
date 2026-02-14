package stream.alchemists.db

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import stream.alchemists.domain.models.Account
import stream.alchemists.domain.models.AccountType
import java.util.*

object Accounts : UUIDTable("accounts") {
    val name = varchar("name", 255)
    val type = varchar("type", 50)
    val balance = double("balance").default(0.0)
    val userId = reference("user_id", Users)
}

class AccountEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    var name by Accounts.name
    var type by Accounts.type
    var balance by Accounts.balance
    var userId by Accounts.userId

    companion object : EntityClass<UUID, AccountEntity>(Accounts) {
        fun toDomain(entity: AccountEntity): Account = Account(
            id = entity.id.toString(),
            name = entity.name,
            type = AccountType.valueOf(entity.type),
            balance = entity.balance,
            userId = entity.userId.toString(),
        )
    }
}
