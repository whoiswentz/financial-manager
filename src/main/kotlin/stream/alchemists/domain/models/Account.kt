package stream.alchemists.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: String,
    val name: String,
    val type: AccountType,
    val balance: Double,
    val userId: String,
)

@Serializable
enum class AccountType {
    CHECKING,
    SAVINGS,
    CREDIT_CARD,
    CASH,
    INVESTMENT,
}
