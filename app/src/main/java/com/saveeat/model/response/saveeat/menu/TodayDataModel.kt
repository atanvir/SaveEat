package com.saveeat.model.response.saveeat.menu

data class TodayDataModel(
    val _id: String,
    val day: String,
    val pickupWindowClose: String,
    val pickupWindowOpen: String,
    val pickupOpenTime: String,
    val pickupCloseTime: String,
    val saleWindowClose: String,
    val saleWindowOpen: String
)