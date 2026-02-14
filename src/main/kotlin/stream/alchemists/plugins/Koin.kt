package stream.alchemists.plugins

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import stream.alchemists.db.DatabaseFactory
import stream.alchemists.db.HikariDatabaseFactory
import stream.alchemists.domain.repositories.AccountRepository
import stream.alchemists.domain.repositories.CategoryRepository
import stream.alchemists.domain.repositories.TransactionRepository
import stream.alchemists.domain.repositories.UserRepository
import stream.alchemists.domain.services.*
import stream.alchemists.infrastructure.AccountRepositoryImpl
import stream.alchemists.infrastructure.CategoryRepositoryImpl
import stream.alchemists.infrastructure.TransactionRepositoryImpl
import stream.alchemists.infrastructure.UserRepositoryImpl
import stream.alchemists.infrastructure.services.*

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(module {
            single<DatabaseFactory> { HikariDatabaseFactory() }
            single<Encryptor> { EncryptorImpl() }
            single<JwtService> { JwtServiceImpl() }
            single<CategoryRepository> { CategoryRepositoryImpl() }
            single<CategoryService> { CategoryServiceImpl(get()) }
            single<UserRepository> { UserRepositoryImpl() }
            single<UserService> { UserServiceImpl(get(), get(), get()) }
            single<AccountRepository> { AccountRepositoryImpl() }
            single<AccountService> { AccountServiceImpl(get()) }
            single<TransactionRepository> { TransactionRepositoryImpl() }
            single<TransactionService> { TransactionServiceImpl(get(), get()) }
        })
    }
}
