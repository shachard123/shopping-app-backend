package com.shopping.models

import kotlinx.serialization.Serializable
import org.bson.Document
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Shop(
    @BsonId val id: String = ObjectId().toString(),
    val ownerId: String, // The user who owns this shop
    val name: String,
    val description: String,
    val phone: String,
    val address: String,
    val paymentDetails: String, // Bank details, crypto, etc.
    val country: String,
    val logoBase64: String? = null // ✅ New field for Base64 image
)

fun Shop.toDocument() :Document{
    val doc = Document()
        .append("_id", id)
        .append("ownerId", ownerId)
        .append("name", name)
        .append("description", description)
        .append("phone", phone)
        .append("address", address)
        .append("paymentDetails", paymentDetails)
        .append("country", country)
    logoBase64?.let { doc.append("logoBase64", it) }
    return doc
}
