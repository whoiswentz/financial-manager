package stream.alchemists.infrastructure

import stream.alchemists.db.UserEntity
import stream.alchemists.db.Users
import stream.alchemists.db.dbQuery
import stream.alchemists.domain.models.User
import stream.alchemists.domain.repositories.UserRepository
import java.util.*

class UserRepositoryImpl : UserRepository {
    override suspend fun create(name: String, email: String, hashedPassword: String): User = dbQuery {
        UserEntity.new {
            this.name = name
            this.email = email
            this.password = hashedPassword
        }.let { UserEntity.toDomain(it) }
    }

    override suspend fun findByEmail(email: String): Pair<User, String>? = dbQuery {
        UserEntity.find { Users.email eq email }
            .firstOrNull()
            ?.let { UserEntity.toDomain(it) to it.password }
    }

    override suspend fun findById(id: UUID): User? = dbQuery {
        UserEntity.findById(id)?.let { UserEntity.toDomain(it) }
    }
}
