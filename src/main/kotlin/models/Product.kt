package com.shopping.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Product(
    @BsonId val id: String = ObjectId().toString(),
    val shopId: String, // The shop selling the product
    val name: String,
    val description: String,
    val category: String,
    val price: Double,
    val stock: Int,
    val colors: List<String>,
    val material: String,
    val dimensions: Dimensions,
    val imageBase64: String // Product image stored as Base64
)

data class Dimensions(
    val length: Double,
    val width: Double,
    val height: Double,
    val weight: Double
)
