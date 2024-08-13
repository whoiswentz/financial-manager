package stream.alchemists.infrastructure

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stream.alchemists.domain.models.Category
import stream.alchemists.domain.models.CreateCategoryRequest
import stream.alchemists.domain.repositories.CategoryRepository
import java.util.*

class CategoryRepositoryImpl : CategoryRepository {
    private val db = mutableListOf<Category>()

    override suspend fun create(request: CreateCategoryRequest): Category = withContext(Dispatchers.IO) {
        val category = Category(
            id = UUID.randomUUID().toString(),
            title = request.title,
            description = request.description,
        )
        db.add(category)

        return@withContext category
    }
}