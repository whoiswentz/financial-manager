package stream.alchemists.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class UpdateAccountRequest(
    val name: String,
    val type: AccountType,
)
