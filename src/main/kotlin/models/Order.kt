package com.shopping.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Order(
    @BsonId val id: String = ObjectId().toString(),
    val userId: String, // Buyer
    val items: List<OrderItem>, // Items from different shops
    val totalPrice: Double,
    val status: String = "pending", // "pending", "shipped", "delivered", "cancelled"
    val createdAt: String = System.currentTimeMillis().toString()
)

data class OrderItem(
    val productId: String,
    val shopId: String, // The shop selling the item
    val quantity: Int,
    val price: Double, // Unit price at time of order
    val status: String = "pending" // Each item can have its own status
)
