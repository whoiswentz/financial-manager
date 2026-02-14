package stream.alchemists.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateTransactionRequest(
    val amount: Double,
    val description: String?,
    val type: TransactionType,
    val categoryId: String?,
    val accountId: String,
)
