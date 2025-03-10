package com.shopping.services

import com.mongodb.client.MongoCollection
import com.shopping.models.Shop
import com.shopping.models.toDocument
import org.bson.Document
import org.bson.types.ObjectId
import org.litote.kmongo.*

class ShopService(private val shopCollection: MongoCollection<Shop>) {

    /** ✅ Create a new shop */
    fun createShop(shop: Shop): String? {
        return try {
            val newShop = shop.toDocument()
            shopCollection.withDocumentClass<Document>().insertOne(newShop)
            newShop["_id"].toString()
        } catch (e: Exception) {
            println("Error creating shop: ${e.message}")
            null
        }
    }

    /** ✅ Get all shops */
    fun getAllShops(): List<Shop> {
        return shopCollection.find().toList()
    }

    /** ✅ Get shop by ID */
    fun getShopById(shopId: String): Shop? {
        return shopCollection.findOne(Shop::id eq shopId)
    }

    /** ✅ Get all shops owned by a specific user */
    fun getShopsByOwner(ownerId: String): List<Shop> {
        return shopCollection.find(Shop::ownerId eq ownerId).toList()
    }

    /** 🛠️ Future Feature: Update Shop */
//    fun updateShop(shopId: String, updatedShop: Shop): Boolean {
//        val result = shopCollection.findOneAndUpdate(Shop::id eq shopId, updatedShop)
//        return result != null
//    }

    /** ✅ Delete a shop */
    fun deleteShop(shopId: String): Boolean {
        val result = shopCollection.findOneAndDelete(Shop::id eq shopId)
        return result != null
    }
}
