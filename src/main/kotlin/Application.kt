package com.shopping

import com.shopping.auth.AuthConfig.configureSecurity
import com.shopping.services.ShopService
import com.shopping.services.UserService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) { // ✅ Enables JSON parsing
        json(Json{
            encodeDefaults = true
            explicitNulls = false
        })
    }
    install(CORS) {
        allowHost("localhost:4200", schemes = listOf("http")) // Specify scheme explicitly
        allowCredentials = true
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Options)
    }

    configureSecurity()
    val userService = UserService(Database.users)
    val shopService = ShopService(Database.shops)
    configureRouting(userService, shopService)
}
