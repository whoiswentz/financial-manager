package stream.alchemists.infrastructure.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import stream.alchemists.AppConfiguration
import stream.alchemists.domain.services.JwtService
import java.util.*

class JwtServiceImpl : JwtService {
    override fun generateToken(userId: String, email: String): String {
        return JWT.create()
            .withAudience(AppConfiguration.jwtAudience)
            .withIssuer(AppConfiguration.jwtIssuer)
            .withClaim("userId", userId)
            .withClaim("email", email)
            .withExpiresAt(Date(System.currentTimeMillis() + AppConfiguration.jwtExpiration))
            .sign(Algorithm.HMAC256(AppConfiguration.jwtSecret))
    }
}
