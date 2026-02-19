package stream.alchemists.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import stream.alchemists.domain.models.CreateCategoryRequest
import stream.alchemists.domain.models.UpdateCategoryRequest
import stream.alchemists.domain.services.CategoryService
import stream.alchemists.utils.getUUIDParameter
import stream.alchemists.utils.getUserId

fun Application.categoryRoutes() {
    val categoryService: CategoryService by inject()

    routing {
        authenticate {
            route("/categories") {
                get {
                    val userId = call.getUserId()
                    val categories = categoryService.findAllByUser(userId)
                    call.respond(HttpStatusCode.OK, categories)
                }
                get("/{id}") {
                    val userId = call.getUserId()
                    val categoryId = call.getUUIDParameter("id") ?: return@get
                    val category = categoryService.findById(userId, categoryId)
                    call.respond(HttpStatusCode.OK, category)
                }
                post {
                    val userId = call.getUserId()
                    val createCategoryRequest = call.receive<CreateCategoryRequest>()
                    val category = categoryService.create(userId, createCategoryRequest)
                    call.respond(HttpStatusCode.Created, category)
                }
                put("/{id}") {
                    val userId = call.getUserId()
                    val categoryId = call.getUUIDParameter("id") ?: return@put
                    val updateCategoryRequest = call.receive<UpdateCategoryRequest>()
                    val updatedCategory = categoryService.update(userId, categoryId, updateCategoryRequest)
                    call.respond(HttpStatusCode.OK, updatedCategory)
                }
                delete("/{id}") {
                    val userId = call.getUserId()
                    val categoryId = call.getUUIDParameter("id") ?: return@delete
                    categoryService.delete(userId, categoryId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}
