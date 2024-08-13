package stream.alchemists

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import stream.alchemists.plugins.configureKoin
import stream.alchemists.plugins.configureRequestValidation
import stream.alchemists.plugins.configureRouting
import stream.alchemists.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureKoin()
    configureSerialization()
    configureRequestValidation()
    configureRouting()
}
