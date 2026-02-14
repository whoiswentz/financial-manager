package stream.alchemists.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: String,
    val amount: Double,
    val description: String?,
    val type: TransactionType,
    val categoryId: String?,
    val accountId: String,
    val userId: String,
    val createdAt: Long,
)

@Serializable
enum class TransactionType {
    INCOME,
    EXPENSE,
}
