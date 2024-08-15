package stream.alchemists.domain.exceptions

class NotFoundException(
    override val message: String,
    override val cause: Throwable? = null
) : BaseApplicationException(message, cause) {

}