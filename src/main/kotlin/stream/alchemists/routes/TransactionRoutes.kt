package stream.alchemists.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import stream.alchemists.domain.models.CreateTransactionRequest
import stream.alchemists.domain.services.TransactionService
import java.util.*

fun Application.transactionRoutes() {
    val transactionService: TransactionService by inject()

    routing {
        authenticate {
            route("/transactions") {
                get {
                    val userId = UUID.fromString(call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString())
                    val transactions = transactionService.findAllByUser(userId)
                    call.respond(HttpStatusCode.OK, transactions)
                }
                get("/{id}") {
                    val userId = UUID.fromString(call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString())
                    val transactionId = UUID.fromString(call.parameters["id"])
                    val transaction = transactionService.findById(userId, transactionId)
                    call.respond(HttpStatusCode.OK, transaction)
                }
                post {
                    val userId = UUID.fromString(call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString())
                    val request = call.receive<CreateTransactionRequest>()
                    val transaction = transactionService.create(userId, request)
                    call.respond(HttpStatusCode.Created, transaction)
                }
                delete("/{id}") {
                    val userId = UUID.fromString(call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString())
                    val transactionId = UUID.fromString(call.parameters["id"])
                    transactionService.delete(userId, transactionId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
            route("/accounts/{accountId}/transactions") {
                get {
                    val userId = UUID.fromString(call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString())
                    val accountId = UUID.fromString(call.parameters["accountId"])
                    val transactions = transactionService.findAllByAccount(userId, accountId)
                    call.respond(HttpStatusCode.OK, transactions)
                }
            }
        }
    }
}
