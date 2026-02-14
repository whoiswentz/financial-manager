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

fun Application.categoryRoutes() {
    val categoryService: CategoryService by inject()

    routing {
        authenticate {
            route("/categories") {
                get {
                    val categories = categoryService.findAll()
                    call.respond(HttpStatusCode.OK, categories)
                }
                get("/{id}") {
                    val categoryId = call.getUUIDParameter("id") ?: return@get
                    val category = categoryService.findById(categoryId)
                    call.respond(HttpStatusCode.OK, category)
                }
                post {
                    val createCategoryRequest = call.receive<CreateCategoryRequest>()
                    val category = categoryService.create(createCategoryRequest)
                    call.respond(HttpStatusCode.Created, category)
                }
                put("/{id}") {
                    val categoryId = call.getUUIDParameter("id") ?: return@put
                    val updateCategoryRequest = call.receive<UpdateCategoryRequest>()
                    val updatedCategory = categoryService.update(categoryId, updateCategoryRequest)
                    call.respond(HttpStatusCode.OK, updatedCategory)
                }
                delete("/{id}") {
                    val categoryId = call.getUUIDParameter("id") ?: return@delete
                    categoryService.delete(categoryId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}
