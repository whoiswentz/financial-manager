package stream.alchemists.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCategoryRequest(
    val title: String,
    val description: String?,
)