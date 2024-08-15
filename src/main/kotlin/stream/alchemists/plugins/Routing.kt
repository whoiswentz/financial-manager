package stream.alchemists.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import stream.alchemists.domain.exceptions.NotFoundException
import stream.alchemists.routes.categoryRoutes

fun Application.configureRouting() {
    install(StatusPages) {
        exception<NotFoundException> { call, cause ->
            val error = cause.toApplicationError(HttpStatusCode.NotFound)
            call.respond(HttpStatusCode.NotFound, error)
        }
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
        exception<RequestValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.reasons.joinToString())
        }
    }

    categoryRoutes()
}