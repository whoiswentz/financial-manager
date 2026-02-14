package stream.alchemists.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import stream.alchemists.domain.models.CreateAccountRequest
import stream.alchemists.domain.models.UpdateAccountRequest
import stream.alchemists.domain.services.AccountService
import stream.alchemists.utils.getUUIDParameter
import stream.alchemists.utils.getUserId

fun Application.accountRoutes() {
    val accountService: AccountService by inject()

    routing {
        authenticate {
            route("/accounts") {
                get {
                    val userId = call.getUserId()
                    val accounts = accountService.findAllByUser(userId)
                    call.respond(HttpStatusCode.OK, accounts)
                }
                get("/{id}") {
                    val userId = call.getUserId()
                    val accountId = call.getUUIDParameter("id") ?: return@get
                    val account = accountService.findById(userId, accountId)
                    call.respond(HttpStatusCode.OK, account)
                }
                post {
                    val userId = call.getUserId()
                    val request = call.receive<CreateAccountRequest>()
                    val account = accountService.create(userId, request)
                    call.respond(HttpStatusCode.Created, account)
                }
                put("/{id}") {
                    val userId = call.getUserId()
                    val accountId = call.getUUIDParameter("id") ?: return@put
                    val request = call.receive<UpdateAccountRequest>()
                    val account = accountService.update(userId, accountId, request)
                    call.respond(HttpStatusCode.OK, account)
                }
                delete("/{id}") {
                    val userId = call.getUserId()
                    val accountId = call.getUUIDParameter("id") ?: return@delete
                    accountService.delete(userId, accountId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}
