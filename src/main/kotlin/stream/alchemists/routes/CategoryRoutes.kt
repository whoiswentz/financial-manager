package stream.alchemists.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import stream.alchemists.domain.models.CreateCategoryRequest
import stream.alchemists.domain.services.CategoryService

fun Application.categoryRoutes() {
    val categoryService: CategoryService by inject()

    routing {
        route("/categories") {
            post {
                val createCategoryRequest = call.receive<CreateCategoryRequest>()
                val result = categoryService.create(createCategoryRequest)

                result.getOrElse {
                    return@post call.respond(HttpStatusCode.BadRequest)
                }.also {
                    return@post call.respond(HttpStatusCode.Created, it)
                }

            }
        }
    }
}