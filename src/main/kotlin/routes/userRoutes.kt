package com.shopping.routes

import com.shopping.auth.AuthConfig
import com.shopping.models.User
import com.shopping.services.UserService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.auth.jwt.*
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class RegisterRequest(val username: String, val password: String)


fun Route.userRoutes(userService: UserService) {

    post("/register") {
        val request = call.receive<RegisterRequest>()
        val userId = userService.createUser(request.username, request.password)

        if (userId != null) {
            call.respond(HttpStatusCode.Created, mapOf("userId" to userId))
        } else {
            call.respond(HttpStatusCode.Conflict, "username already exists")
        }
    }

    post("/login") {
        val request = call.receive<LoginRequest>()
        val user = userService.validateUser(request.username, request.password)

        if (user != null) {
            val token = AuthConfig.generateToken(user.id)
            call.respond(mapOf("token" to token))
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
        }
    }

    authenticate {
        get("/me") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("userId")?.asString()
            val user = userService.getUserById(userId ?: "")

            if (user != null) {
                call.respond(user)
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found")
            }
        }
    }
}
