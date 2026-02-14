package stream.alchemists.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import stream.alchemists.AppConfiguration

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt {
            realm = AppConfiguration.jwtRealm
            verifier(
                JWT.require(Algorithm.HMAC256(AppConfiguration.jwtSecret))
                    .withAudience(AppConfiguration.jwtAudience)
                    .withIssuer(AppConfiguration.jwtIssuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("userId").asString() != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("message" to "Token is not valid or has expired"))
            }
        }
    }
}
