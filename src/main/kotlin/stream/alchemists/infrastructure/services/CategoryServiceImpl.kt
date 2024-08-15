package stream.alchemists.infrastructure.services

import stream.alchemists.domain.models.Category
import stream.alchemists.domain.models.CreateCategoryRequest
import stream.alchemists.domain.models.UpdateCategoryRequest
import stream.alchemists.domain.repositories.CategoryRepository
import stream.alchemists.domain.services.CategoryService
import java.util.UUID

class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
) : CategoryService {
    override suspend fun create(request: CreateCategoryRequest): Category {
        return categoryRepository.create(request)
    }

    override suspend fun update(id: UUID, request: UpdateCategoryRequest): Category {
        return categoryRepository.update(id, request)
    }
}