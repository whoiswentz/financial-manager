package stream.alchemists.infrastructure.services

import stream.alchemists.domain.models.Category
import stream.alchemists.domain.models.CreateCategoryRequest
import stream.alchemists.domain.repositories.CategoryRepository
import stream.alchemists.domain.services.CategoryService

class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
) : CategoryService {
    override suspend fun create(request: CreateCategoryRequest): Result<Category> {
        val category = categoryRepository.create(request)
        return Result.success(category)
    }
}