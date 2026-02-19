package stream.alchemists

import io.ktor.server.application.*
import io.ktor.server.netty.*
import stream.alchemists.db.HikariDatabaseFactory
import stream.alchemists.plugins.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureDatabase(HikariDatabaseFactory())
    configureKoin()
    configureSerialization()
    configureAuthentication()
    configureRequestValidation()
    configureRouting()
}
