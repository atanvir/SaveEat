package com.saveeat.model.response.saveeat.menu

data class MenuExtraModel(
    val _id: String,
    val cuisine: String,
    val description: String,
    val foodImage: String,
    val foodName: String,
    val foodQuantity: Int,
    val foodType: String,
    val price: String,
    val check: Boolean,
    val subCategory: String
)