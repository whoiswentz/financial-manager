package stream.alchemists.infrastructure

import stream.alchemists.db.CategoryEntity
import stream.alchemists.db.dbQuery
import stream.alchemists.domain.models.Category
import stream.alchemists.domain.models.CreateCategoryRequest
import stream.alchemists.domain.repositories.CategoryRepository

class CategoryRepositoryImpl : CategoryRepository {
    override suspend fun create(request: CreateCategoryRequest): Category = dbQuery {
        val category = CategoryEntity.new {
            title = request.title
            description = request.description
        }.let { CategoryEntity.toDomain(it) }

        return@dbQuery category
    }
}