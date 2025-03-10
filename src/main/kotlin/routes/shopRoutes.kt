package com.shopping.routes

import com.shopping.models.Shop
import com.shopping.services.ShopService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class ShopRequest(
    val name: String,
    val description: String,
    val phone: String,
    val address: String,
    val paymentDetails: String,
    val country: String,
    val logoBase64: String? = null
)

fun Route.shopRoutes(shopService: ShopService) {

    /** ✅ Get all shops */
    get("") {
        val shops = shopService.getAllShops()
        call.respond(shops)
    }

    /** ✅ Get shop by ID */
    get("/{id}") {
        val shopId = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Shop ID required")
        val shop = shopService.getShopById(shopId)
        if (shop != null) {
            call.respond(shop)
        } else {
            call.respond(HttpStatusCode.NotFound, "Shop not found")
        }
    }

    /** ✅ Get all shops owned by the logged-in user */
    authenticate {
        get("/my") {
            val principal = call.principal<JWTPrincipal>()
            val ownerId = principal?.payload?.getClaim("userId")?.asString()

            if (ownerId != null) {
                val userShops = shopService.getShopsByOwner(ownerId)
                call.respond(userShops)
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Not authenticated")
            }
        }
    }

    /** ✅ Create a new shop */
    authenticate {
        post("") {
            val principal = call.principal<JWTPrincipal>()
            val ownerId = principal?.payload?.getClaim("userId")?.asString()

            if (ownerId == null) {
                call.respond(HttpStatusCode.Unauthorized, "Not authenticated")
                return@post
            }

            val request = call.receive<ShopRequest>()
            val newShop = Shop(
                ownerId = ownerId,
                name = request.name,
                description = request.description,
                phone = request.phone,
                address = request.address,
                paymentDetails = request.paymentDetails,
                country = request.country,
                logoBase64 = request.logoBase64
            )

            val shopId = shopService.createShop(newShop)
            if (shopId != null) {
                call.respond(HttpStatusCode.Created, mapOf("shopId" to shopId))
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Failed to create shop")
            }
        }
    }

//    /** 🛠️ Future Feature: Update a shop */
//    authenticate {
//        put("/shops/{id}") {
//            val principal = call.principal<JWTPrincipal>()
//            val ownerId = principal?.payload?.getClaim("userId")?.asString()
//            val shopId = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest, "Shop ID required")
//
//            val shop = shopService.getShopById(shopId)
//
//            if (shop == null) {
//                call.respond(HttpStatusCode.NotFound, "Shop not found")
//                return@put
//            }
//
//            if (shop.ownerId != ownerId) {
//                call.respond(HttpStatusCode.Forbidden, "You do not own this shop")
//                return@put
//            }
//
//            val updatedShop = call.receive<ShopRequest>()
//            val success = shopService.updateShop(
//                shopId,
//                Shop(
//                    id = shopId,
//                    ownerId = ownerId!!,
//                    name = updatedShop.name,
//                    description = updatedShop.description,
//                    phone = updatedShop.phone,
//                    address = updatedShop.address,
//                    paymentDetails = updatedShop.paymentDetails,
//                    country = updatedShop.country
//                )
//            )
//
//            if (success) {
//                call.respond(HttpStatusCode.OK, "Shop updated")
//            } else {
//                call.respond(HttpStatusCode.InternalServerError, "Failed to update shop")
//            }
//        }
//    }

    /** ✅ Delete a shop */
    authenticate {
        delete("/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val ownerId = principal?.payload?.getClaim("userId")?.asString()
            val shopId = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "Shop ID required")

            val shop = shopService.getShopById(shopId)

            if (shop == null) {
                call.respond(HttpStatusCode.NotFound, "Shop not found")
                return@delete
            }

            if (shop.ownerId != ownerId) {
                call.respond(HttpStatusCode.Forbidden, "You do not own this shop")
                return@delete
            }

            val success = shopService.deleteShop(shopId)
            if (success) {
                call.respond(HttpStatusCode.OK, mapOf("message" to "Shop deleted"))
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Failed to delete shop")
            }
        }
    }
}
