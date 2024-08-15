package stream.alchemists.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import stream.alchemists.domain.models.CreateCategoryRequest
import stream.alchemists.domain.models.UpdateCategoryRequest
import stream.alchemists.domain.services.CategoryService
import java.util.*

fun Application.categoryRoutes() {
    val categoryService: CategoryService by inject()

    routing {
        route("/categories") {
            post {
                val createCategoryRequest = call.receive<CreateCategoryRequest>()
                val category = categoryService.create(createCategoryRequest)
                return@post call.respond(HttpStatusCode.Created, category)
            }
            put("/{id}") {
                val categoryId = UUID.fromString(call.parameters["id"])
                val updateCategoryRequest = call.receive<UpdateCategoryRequest>()
                val updatedCategory = categoryService.update(categoryId, updateCategoryRequest)
                return@put call.respond(HttpStatusCode.OK, updatedCategory)
            }
        }
    }
}