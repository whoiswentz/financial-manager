package stream.alchemists.infrastructure

import stream.alchemists.db.CategoryEntity
import stream.alchemists.db.dbQuery
import stream.alchemists.domain.exceptions.NotFoundException
import stream.alchemists.domain.models.Category
import stream.alchemists.domain.models.CreateCategoryRequest
import stream.alchemists.domain.models.UpdateCategoryRequest
import stream.alchemists.domain.repositories.CategoryRepository
import java.util.*

class CategoryRepositoryImpl : CategoryRepository {
    override suspend fun create(request: CreateCategoryRequest): Category = dbQuery {
        val category = CategoryEntity.new {
            title = request.title
            description = request.description
        }.let { CategoryEntity.toDomain(it) }

        return@dbQuery category
    }

    override suspend fun update(id: UUID, request: UpdateCategoryRequest): Category = dbQuery {
        val category = CategoryEntity.findById(id)
            ?: throw NotFoundException("category with $id not found")

        category.title = request.title
        category.description = request.description

        return@dbQuery CategoryEntity.toDomain(category)
    }
}