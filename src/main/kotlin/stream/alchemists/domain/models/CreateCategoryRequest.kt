package stream.alchemists.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateCategoryRequest(
    val title: String,
    val description: String
)
