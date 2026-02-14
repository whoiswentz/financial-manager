package stream.alchemists.domain.services

import stream.alchemists.domain.models.Category
import stream.alchemists.domain.models.CreateCategoryRequest
import stream.alchemists.domain.models.UpdateCategoryRequest
import java.util.*

interface CategoryService {
    suspend fun create(request: CreateCategoryRequest): Category
    suspend fun findAll(): List<Category>
    suspend fun findById(id: UUID): Category
    suspend fun update(id: UUID, request: UpdateCategoryRequest): Category
    suspend fun delete(id: UUID)
}