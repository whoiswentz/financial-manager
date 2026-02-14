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
        CategoryEntity.new {
            title = request.title
            description = request.description
        }.let { CategoryEntity.toDomain(it) }
    }

    override suspend fun findAll(): List<Category> = dbQuery {
        CategoryEntity.all().map { CategoryEntity.toDomain(it) }
    }

    override suspend fun findById(id: UUID): Category? = dbQuery {
        CategoryEntity.findById(id)?.let { CategoryEntity.toDomain(it) }
    }

    override suspend fun update(id: UUID, request: UpdateCategoryRequest): Category = dbQuery {
        val category = CategoryEntity.findById(id)
            ?: throw NotFoundException("category with $id not found")

        category.title = request.title
        category.description = request.description

        CategoryEntity.toDomain(category)
    }

    override suspend fun delete(id: UUID) = dbQuery {
        val category = CategoryEntity.findById(id)
            ?: throw NotFoundException("category with $id not found")
        category.delete()
    }
}
