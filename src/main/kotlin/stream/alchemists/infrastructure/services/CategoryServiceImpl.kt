package stream.alchemists.infrastructure.services

import stream.alchemists.domain.exceptions.NotFoundException
import stream.alchemists.domain.models.Category
import stream.alchemists.domain.models.CreateCategoryRequest
import stream.alchemists.domain.models.UpdateCategoryRequest
import stream.alchemists.domain.repositories.CategoryRepository
import stream.alchemists.domain.services.CategoryService
import java.util.UUID

class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
) : CategoryService {
    override suspend fun create(userId: UUID, request: CreateCategoryRequest): Category {
        return categoryRepository.create(userId, request)
    }

    override suspend fun findAllByUser(userId: UUID): List<Category> {
        return categoryRepository.findAllByUser(userId)
    }

    override suspend fun findById(userId: UUID, id: UUID): Category {
        return categoryRepository.findById(userId, id)
            ?: throw NotFoundException("category with $id not found")
    }

    override suspend fun update(userId: UUID, id: UUID, request: UpdateCategoryRequest): Category {
        return categoryRepository.update(userId, id, request)
    }

    override suspend fun delete(userId: UUID, id: UUID) {
        categoryRepository.delete(userId, id)
    }
}
