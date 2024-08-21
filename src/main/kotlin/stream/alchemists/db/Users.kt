package stream.alchemists.db

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.UUID

object Users : UUIDTable("users") {
    val name = varchar("name", 255)
    val email = text("email")
    val password = text("password")
}

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    var name by Users.name
    var email by Users.email
    var password by Users.password

    companion object : EntityClass<UUID, UserEntity>(Users)
}