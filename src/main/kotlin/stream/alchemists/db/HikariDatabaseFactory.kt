package stream.alchemists.db

import AppConfiguration
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

class HikariDatabaseFactory : DatabaseFactory {
    override fun createDataSource(): DataSource {
        val config = HikariConfig().apply {
            driverClassName = AppConfiguration.dbDriver
            jdbcUrl = AppConfiguration.dbUrl
            username = AppConfiguration.dbUser
            password = AppConfiguration.dbPassword
            maximumPoolSize = AppConfiguration.dbMaximumPoolSize
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        return HikariDataSource(config)
    }
}