package stream.alchemists.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import stream.alchemists.domain.models.CreateTransactionRequest
import stream.alchemists.domain.services.TransactionService
import stream.alchemists.utils.getUUIDParameter
import stream.alchemists.utils.getUserId

fun Application.transactionRoutes() {
    val transactionService: TransactionService by inject()

    routing {
        authenticate {
            route("/transactions") {
                get {
                    val userId = call.getUserId()
                    val transactions = transactionService.findAllByUser(userId)
                    call.respond(HttpStatusCode.OK, transactions)
                }
                get("/{id}") {
                    val userId = call.getUserId()
                    val transactionId = call.getUUIDParameter("id") ?: return@get
                    val transaction = transactionService.findById(userId, transactionId)
                    call.respond(HttpStatusCode.OK, transaction)
                }
                post {
                    val userId = call.getUserId()
                    val request = call.receive<CreateTransactionRequest>()
                    val transaction = transactionService.create(userId, request)
                    call.respond(HttpStatusCode.Created, transaction)
                }
                delete("/{id}") {
                    val userId = call.getUserId()
                    val transactionId = call.getUUIDParameter("id") ?: return@delete
                    transactionService.delete(userId, transactionId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
            route("/accounts/{accountId}/transactions") {
                get {
                    val userId = call.getUserId()
                    val accountId = call.getUUIDParameter("accountId") ?: return@get
                    val transactions = transactionService.findAllByAccount(userId, accountId)
                    call.respond(HttpStatusCode.OK, transactions)
                }
            }
        }
    }
}
