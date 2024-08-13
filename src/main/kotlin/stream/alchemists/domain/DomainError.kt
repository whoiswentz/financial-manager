package stream.alchemists.domain

sealed class DomainError(val message: String) {
    class NotFound(message: String) : DomainError(message)
    class BadRequest(message: String) : DomainError(message)
}