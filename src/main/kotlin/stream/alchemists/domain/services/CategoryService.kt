package stream.alchemists.domain.services

import stream.alchemists.domain.models.Category
import stream.alchemists.domain.models.CreateCategoryRequest
import stream.alchemists.domain.models.UpdateCategoryRequest
import java.util.*

interface CategoryService {
    suspend fun create(userId: UUID, request: CreateCategoryRequest): Category
    suspend fun findAllByUser(userId: UUID): List<Category>
    suspend fun findById(userId: UUID, id: UUID): Category
    suspend fun update(userId: UUID, id: UUID, request: UpdateCategoryRequest): Category
    suspend fun delete(userId: UUID, id: UUID)
}