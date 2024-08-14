package stream.alchemists.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import stream.alchemists.db.HikariDatabaseFactory

fun Application.configureDatabase(factory: HikariDatabaseFactory) {
    val databaseFactory = factory.createDataSource()
    Database.connect(databaseFactory)
}