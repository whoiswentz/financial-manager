package stream.alchemists.domain.services

import stream.alchemists.domain.models.Category
import stream.alchemists.domain.models.CreateCategoryRequest

interface CategoryService {
    suspend fun create(request: CreateCategoryRequest): Result<Category>
}