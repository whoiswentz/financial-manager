package stream.alchemists.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import stream.alchemists.domain.exceptions.ConflictException
import stream.alchemists.domain.exceptions.NotFoundException
import stream.alchemists.domain.exceptions.UnauthorizedException
import stream.alchemists.routes.accountRoutes
import stream.alchemists.routes.authRoutes
import stream.alchemists.routes.categoryRoutes
import stream.alchemists.routes.transactionRoutes

fun Application.configureRouting() {
    install(StatusPages) {
        exception<NotFoundException> { call, cause ->
            val error = cause.toApplicationError(HttpStatusCode.NotFound)
            call.respond(HttpStatusCode.NotFound, error)
        }
        exception<ConflictException> { call, cause ->
            val error = cause.toApplicationError(HttpStatusCode.Conflict)
            call.respond(HttpStatusCode.Conflict, error)
        }
        exception<UnauthorizedException> { call, cause ->
            val error = cause.toApplicationError(HttpStatusCode.Unauthorized)
            call.respond(HttpStatusCode.Unauthorized, error)
        }
        exception<RequestValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.reasons.joinToString())
        }
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }

    authRoutes()
    categoryRoutes()
    accountRoutes()
    transactionRoutes()
}
