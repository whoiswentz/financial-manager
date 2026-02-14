package stream.alchemists.domain.services

import stream.alchemists.domain.models.Account
import stream.alchemists.domain.models.CreateAccountRequest
import stream.alchemists.domain.models.UpdateAccountRequest
import java.util.*

interface AccountService {
    suspend fun create(userId: UUID, request: CreateAccountRequest): Account
    suspend fun findAllByUser(userId: UUID): List<Account>
    suspend fun findById(userId: UUID, id: UUID): Account
    suspend fun update(userId: UUID, id: UUID, request: UpdateAccountRequest): Account
    suspend fun delete(userId: UUID, id: UUID)
}
