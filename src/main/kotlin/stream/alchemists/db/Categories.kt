package stream.alchemists.db

import org.jetbrains.exposed.dao.id.UUIDTable

object Categories : UUIDTable("categories") {
    val title = varchar("title", 80)
    val description = varchar("description", 255).nullable()
}