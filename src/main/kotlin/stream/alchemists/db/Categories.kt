package stream.alchemists.db

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import stream.alchemists.domain.models.Category
import java.util.*

object Categories : UUIDTable("categories") {
    val title = varchar("title", 80)
    val description = varchar("description", 255).nullable()
}

class CategoryEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    var title by Categories.title
    var description by Categories.description

    companion object : EntityClass<UUID, CategoryEntity>(Categories) {
        fun toDomain(entity: CategoryEntity): Category = with(entity) {
            Category(
                id = entity.id.toString(),
                title = entity.title,
                description = entity.description
            )
        }
    }
}