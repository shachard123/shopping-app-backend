package com.shopping.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.jwt.*
import io.ktor.server.auth.*
import io.ktor.server.application.*


object AuthConfig {
    private const val secret = "your_secret_key"
    private const val issuer = "shoppingApp"
    private const val audience = "shoppingUsers"

    fun generateToken(userId: String): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", userId)
            .sign(Algorithm.HMAC256(secret))
    }

    fun Application.configureSecurity() {
        authentication {
            jwt {
                realm = "shoppingApp"
                verifier(
                    JWT.require(Algorithm.HMAC256(secret))
                        .withIssuer(issuer)
                        .withAudience(audience)
                        .build()
                )
                validate { credential ->
                    if (credential.payload.getClaim("userId").asString() != null) {
                        JWTPrincipal(credential.payload)
                    } else null
                }
            }
        }
    }
}
