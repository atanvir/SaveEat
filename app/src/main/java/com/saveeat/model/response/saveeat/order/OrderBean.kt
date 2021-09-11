package com.saveeat.model.response.saveeat.order

import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import kotlinx.coroutines.sync.Mutex

data class OrderBean(
    val _id: String,
    val convertedPickupDate: String,
    val createdAt: String,
    val orderDeliveredTime: String,
    val orderData: MutableList<OrderData?>?,
    val orderNumber: String,
    val pickupDate: String,
    val pickupTime: String,
    val pin: String,
    val qrCode: String,
    var ratingData: MutableList<Any>,
    val restroData: RestaurantResponseBean?,
    val saveAmount: Double?,
    val status: String,
    val storeId: String,
    val subTotal: Double,
    val tax: Double,
    val total: Double,
    val userId: String
)