package stream.alchemists

import io.ktor.server.application.*
import io.ktor.server.netty.*
import stream.alchemists.db.HikariDatabaseFactory
import stream.alchemists.plugins.configureDatabase
import stream.alchemists.plugins.configureKoin
import stream.alchemists.plugins.configureRequestValidation
import stream.alchemists.plugins.configureRouting
import stream.alchemists.plugins.configureSerialization

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureDatabase(HikariDatabaseFactory())
    configureKoin()
    configureSerialization()
    configureRequestValidation()
    configureRouting()
}
