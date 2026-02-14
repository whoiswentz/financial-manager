package stream.alchemists.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import stream.alchemists.domain.models.CreateAccountRequest
import stream.alchemists.domain.models.UpdateAccountRequest
import stream.alchemists.domain.services.AccountService
import java.util.*

fun Application.accountRoutes() {
    val accountService: AccountService by inject()

    routing {
        authenticate {
            route("/accounts") {
                get {
                    val userId = UUID.fromString(call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString())
                    val accounts = accountService.findAllByUser(userId)
                    call.respond(HttpStatusCode.OK, accounts)
                }
                get("/{id}") {
                    val userId = UUID.fromString(call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString())
                    val accountId = UUID.fromString(call.parameters["id"])
                    val account = accountService.findById(userId, accountId)
                    call.respond(HttpStatusCode.OK, account)
                }
                post {
                    val userId = UUID.fromString(call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString())
                    val request = call.receive<CreateAccountRequest>()
                    val account = accountService.create(userId, request)
                    call.respond(HttpStatusCode.Created, account)
                }
                put("/{id}") {
                    val userId = UUID.fromString(call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString())
                    val accountId = UUID.fromString(call.parameters["id"])
                    val request = call.receive<UpdateAccountRequest>()
                    val account = accountService.update(userId, accountId, request)
                    call.respond(HttpStatusCode.OK, account)
                }
                delete("/{id}") {
                    val userId = UUID.fromString(call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString())
                    val accountId = UUID.fromString(call.parameters["id"])
                    accountService.delete(userId, accountId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}
