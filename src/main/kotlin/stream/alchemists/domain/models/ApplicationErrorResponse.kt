package stream.alchemists.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationErrorResponse(
    val code: Int,
    val message: String?
)