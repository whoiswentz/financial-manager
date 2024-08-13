package stream.alchemists.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import stream.alchemists.domain.models.CreateCategoryRequest

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        validate<CreateCategoryRequest> { request ->
            if (request.title.isBlank()) {
                ValidationResult.Invalid("Title can not be empty")
            }
            ValidationResult.Valid
        }
    }
}