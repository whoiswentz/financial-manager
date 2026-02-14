package stream.alchemists.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import stream.alchemists.domain.models.CreateAccountRequest
import stream.alchemists.domain.models.CreateCategoryRequest
import stream.alchemists.domain.models.CreateTransactionRequest
import stream.alchemists.domain.models.RegisterRequest

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        validate<CreateCategoryRequest> { request ->
            if (request.title.isBlank()) {
                ValidationResult.Invalid("Title can not be empty")
            } else {
                ValidationResult.Valid
            }
        }
        validate<RegisterRequest> { request ->
            val reasons = mutableListOf<String>()
            if (request.name.isBlank()) reasons.add("Name can not be empty")
            if (request.email.isBlank()) reasons.add("Email can not be empty")
            if (request.password.length < 6) reasons.add("Password must be at least 6 characters")
            if (reasons.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(reasons)
        }
        validate<CreateAccountRequest> { request ->
            if (request.name.isBlank()) {
                ValidationResult.Invalid("Account name can not be empty")
            } else {
                ValidationResult.Valid
            }
        }
        validate<CreateTransactionRequest> { request ->
            val reasons = mutableListOf<String>()
            if (request.amount <= 0) reasons.add("Amount must be greater than 0")
            if (request.accountId.isBlank()) reasons.add("Account ID is required")
            if (reasons.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(reasons)
        }
    }
}
