package stream.alchemists.domain.repositories

import stream.alchemists.domain.models.Category
import stream.alchemists.domain.models.CreateCategoryRequest
import stream.alchemists.domain.models.UpdateCategoryRequest
import java.util.*

interface CategoryRepository {
    suspend fun create(request: CreateCategoryRequest): Category
    suspend fun update(id: UUID, request: UpdateCategoryRequest): Category
}