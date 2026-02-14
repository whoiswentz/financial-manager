package stream.alchemists.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateAccountRequest(
    val name: String,
    val type: AccountType,
)
