package stream.alchemists.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import java.util.*

fun ApplicationCall.getUserId(): UUID {
    return UUID.fromString(principal<JWTPrincipal>()!!.payload.getClaim("userId").asString())
}

suspend fun ApplicationCall.getUUIDParameter(name: String): UUID? {
    return try {
        UUID.fromString(parameters[name])
    } catch (e: IllegalArgumentException) {
        respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid UUID format for parameter '$name'"))
        null
    }
}
