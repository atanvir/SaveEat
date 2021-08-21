package com.saveeat.model.response.saveeat.main.home

data class RestaurantProductModel(
    val _id: String,
    val description: String,
    val discountAmount: Double,
    val discountPer: Int,
    val foodImage: String,
    val foodName: String,
    val foodQuantity: Int,
    val foodType: String,
    val leftQuantity: Int,
    val menuCategoryName: String,
    val menuId: String,
    val price: Double,
    val quantitySell: Int,
)