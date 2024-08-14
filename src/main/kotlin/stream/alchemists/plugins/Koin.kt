package stream.alchemists.plugins

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import stream.alchemists.db.DatabaseFactory
import stream.alchemists.db.HikariDatabaseFactory
import stream.alchemists.domain.repositories.CategoryRepository
import stream.alchemists.domain.services.CategoryService
import stream.alchemists.infrastructure.CategoryRepositoryImpl
import stream.alchemists.infrastructure.services.CategoryServiceImpl

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(module {
            single<DatabaseFactory> { HikariDatabaseFactory() }
            single<CategoryRepository> { CategoryRepositoryImpl() }
            single<CategoryService> { CategoryServiceImpl(get()) }
        })
    }
}