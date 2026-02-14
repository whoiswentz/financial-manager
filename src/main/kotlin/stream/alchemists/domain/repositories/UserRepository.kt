package stream.alchemists.domain.repositories

import stream.alchemists.domain.models.User
import java.util.*

interface UserRepository {
    suspend fun create(name: String, email: String, hashedPassword: String): User
    suspend fun findByEmail(email: String): Pair<User, String>?
    suspend fun findById(id: UUID): User?
}
