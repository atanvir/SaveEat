package com.saveeat.model.response.saveeat.order

data class RatingModel(
    val __v: Int,
    val _id: String,
    val brandId: String,
    val createdAt: String,
    val orderId: String,
    val rating: Double,
    val review: String,
    val status: String,
    val updatedAt: String,
    val userId: String
)