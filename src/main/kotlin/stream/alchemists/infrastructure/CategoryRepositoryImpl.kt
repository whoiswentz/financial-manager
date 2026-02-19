package stream.alchemists.infrastructure

import org.jetbrains.exposed.dao.id.EntityID
import stream.alchemists.db.Categories
import stream.alchemists.db.CategoryEntity
import stream.alchemists.db.Users
import stream.alchemists.db.dbQuery
import stream.alchemists.domain.exceptions.NotFoundException
import stream.alchemists.domain.models.Category
import stream.alchemists.domain.models.CreateCategoryRequest
import stream.alchemists.domain.models.UpdateCategoryRequest
import stream.alchemists.domain.repositories.CategoryRepository
import java.util.*

class CategoryRepositoryImpl : CategoryRepository {
    override suspend fun create(userId: UUID, request: CreateCategoryRequest): Category = dbQuery {
        CategoryEntity.new {
            title = request.title
            description = request.description
            this.userId = EntityID(userId, Users)
        }.let { CategoryEntity.toDomain(it) }
    }

    override suspend fun findAllByUser(userId: UUID): List<Category> = dbQuery {
        CategoryEntity.find { Categories.userId eq userId }
            .map { CategoryEntity.toDomain(it) }
    }

    override suspend fun findById(userId: UUID, id: UUID): Category? = dbQuery {
        CategoryEntity.findById(id)?.let { entity ->
            if (entity.userId.value != userId) null
            else CategoryEntity.toDomain(entity)
        }
    }

    override suspend fun update(userId: UUID, id: UUID, request: UpdateCategoryRequest): Category = dbQuery {
        val category = CategoryEntity.findById(id)
            ?: throw NotFoundException("category with $id not found")
        
        if (category.userId.value != userId) {
            throw NotFoundException("category with $id not found")
        }

        category.title = request.title
        category.description = request.description

        CategoryEntity.toDomain(category)
    }

    override suspend fun delete(userId: UUID, id: UUID) = dbQuery {
        val category = CategoryEntity.findById(id)
            ?: throw NotFoundException("category with $id not found")
        
        if (category.userId.value != userId) {
            throw NotFoundException("category with $id not found")
        }
        
        category.delete()
    }
}
