package stream.alchemists.domain.services

interface JwtService {
    fun generateToken(userId: String, email: String): String
}
