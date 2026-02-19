package stream.alchemists.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import stream.alchemists.domain.models.LoginRequest
import stream.alchemists.domain.models.RegisterRequest
import stream.alchemists.domain.services.UserService

fun Application.authRoutes() {
    val userService: UserService by inject()

    routing {
        route("/auth") {
            post("/register") {
                val request = call.receive<RegisterRequest>()
                val response = userService.register(request)
                call.respond(HttpStatusCode.Created, response)
            }
            post("/login") {
                val request = call.receive<LoginRequest>()
                val response = userService.login(request)
                call.respond(HttpStatusCode.OK, response)
            }
        }
    }
}
