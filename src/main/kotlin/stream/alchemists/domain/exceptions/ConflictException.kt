package stream.alchemists.domain.exceptions

class ConflictException(
    override val message: String,
    override val cause: Throwable? = null
) : BaseApplicationException(message, cause)
