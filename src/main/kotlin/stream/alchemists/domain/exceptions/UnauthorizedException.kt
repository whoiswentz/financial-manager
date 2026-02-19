package stream.alchemists.domain.exceptions

class UnauthorizedException(
    override val message: String,
    override val cause: Throwable? = null
) : BaseApplicationException(message, cause)
