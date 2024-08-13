package stream.alchemists.domain.repositories

import stream.alchemists.domain.models.Category
import stream.alchemists.domain.models.CreateCategoryRequest

interface CategoryRepository {
    suspend fun create(request: CreateCategoryRequest): Category
}