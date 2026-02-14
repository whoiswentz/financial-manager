package stream.alchemists.domain.repositories

import stream.alchemists.domain.models.Account
import stream.alchemists.domain.models.CreateAccountRequest
import stream.alchemists.domain.models.UpdateAccountRequest
import java.util.*

interface AccountRepository {
    suspend fun create(userId: UUID, request: CreateAccountRequest): Account
    suspend fun findAllByUser(userId: UUID): List<Account>
    suspend fun findById(id: UUID): Account?
    suspend fun update(id: UUID, request: UpdateAccountRequest): Account
    suspend fun delete(id: UUID)
    suspend fun updateBalance(id: UUID, delta: Double)
}
