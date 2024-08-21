package stream.alchemists

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

object AppConfiguration {
    private val applicationConfig = HoconApplicationConfig(ConfigFactory.load())

    val dbUrl = applicationConfig.property("db.jdbcUrl").getString()
    val dbUser = applicationConfig.property("db.user").getString()
    val dbPassword = applicationConfig.property("db.password").getString()
    val dbDriver = applicationConfig.property("db.driver").getString()
    val dbMaximumPoolSize = applicationConfig.property("db.maximumPoolSize").getString().toInt()

    val secretKey = applicationConfig.property("encryptor.secret_key").getString()
    val algorithm = applicationConfig.property("encryptor.algorithm").getString()
    val iterations = applicationConfig.property("encryptor.iterations").getString().toInt()
    val keyLength = applicationConfig.property("encryptor.keyLength").getString().toInt()
}