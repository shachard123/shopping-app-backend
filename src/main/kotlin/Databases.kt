package com.shopping

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.shopping.models.User
import com.shopping.models.Shop
import com.shopping.models.Product
import com.shopping.models.Order
import com.shopping.services.UserService
import com.shopping.services.ShopService

import io.ktor.server.application.*
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

object Database {
    private val host = System.getenv("MONGO_HOST") ?: "127.0.0.1"
    private val port = System.getenv("MONGO_PORT") ?: "27017"
    private val databaseName = System.getenv("MONGO_DB_NAME") ?: "shoppingApp"


    private val uri = "mongodb://$host:$port"
    private val mongoClient = KMongo.createClient(uri) // ✅ Uses KMongo

    val db: MongoDatabase = mongoClient.getDatabase(databaseName)

    val users = db.getCollection<User>() // ✅ Properly typed collection
    val shops = db.getCollection<Shop>()
    val products = db.getCollection<Product>()
    val orders = db.getCollection<Order>()

    fun closeConnection() {
        mongoClient.close()
    }
}