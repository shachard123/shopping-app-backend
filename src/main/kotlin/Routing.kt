package com.shopping

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mongodb.client.*
import com.shopping.routes.userRoutes
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.shopping.services.UserService


fun Application.configureRouting(userService: UserService) {
    routing {
        route("/users"){
            userRoutes(userService)
        }
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
