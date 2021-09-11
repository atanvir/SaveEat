package com.saveeat.model.request.order

data class CartDataPlaceOrderModel(
    val actualAmount: Double?,
    val address: String?,
    val cartId: String?,
    val discountAmount: Double?,
    val latitude: Double?,
    val longitude: Double?,
    val orderType: String?,
    val pickupDate: String?,
    val pickupTime: String?,
    val price: Double?,
    val restroId: String?,
    val saveAmount: Double?,
    val subTotal: Double?,
    val tax: Double?,
    val timezone: String?,
    var total: Double?
)