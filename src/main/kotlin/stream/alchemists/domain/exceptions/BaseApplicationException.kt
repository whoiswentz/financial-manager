package stream.alchemists.domain.exceptions

import io.ktor.http.*
import stream.alchemists.domain.models.ApplicationErrorResponse

abstract class BaseApplicationException : Exception {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable) : super(cause)

    fun toApplicationError(code: HttpStatusCode): ApplicationErrorResponse {
        return ApplicationErrorResponse(
            code = code.value,
            message = this.message
        )
    }
}