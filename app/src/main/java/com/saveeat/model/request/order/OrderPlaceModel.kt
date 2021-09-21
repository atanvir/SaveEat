package com.saveeat.model.request.order

data class OrderPlaceModel(
    var cartData : MutableList<CartDataPlaceOrderModel?>?,
    val paymentId: String?,
    val paymentMode: String?,
    val paymentStatus: String?,
    val saveAmount: Double?,
    val subTotal: Double?,
    val tax: Double?,
    val total: Double?,
    val token : String?,
    val userId: String?,
    val saveEatFees:Double?,
    var taxes: Double?
)