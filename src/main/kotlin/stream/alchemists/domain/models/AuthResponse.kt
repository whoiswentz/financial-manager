package stream.alchemists.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
)
