package com.saveeat.model.response.saveeat.order

data class OrderHistoryModel(
    val data: MutableList<OrderBean?>?,
    val message: String,
    val status: Int
)