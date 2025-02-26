package com.shopping.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Shop(
    @BsonId val id: String = ObjectId().toString(),
    val ownerId: String, // The user who owns this shop
    val name: String,
    val description: String,
    val phone: String,
    val address: String,
    val paymentDetails: String, // Bank details, crypto, etc.
    val country: String
)
