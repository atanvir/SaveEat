package com.saveeat.model.response.saveeat.auth

data class AuthBean(
    val __v: Int,
    val _id: String,
    val userId: String,
    val address: String,
    val countryCode: String,
    val createdAt: String,
    val deleteStatus: Boolean,
    val deviceToken: String,
    val deviceType: String,
    val email: String,
    val foodRescure: Int,
    val jwtToken: String,
    var distance: Int?,
    val latitude: String,
    val location: Location,
    val longitude: String,
    val mobileNumber: String,
    val name: String,
    val notificationStatus: Boolean,
    val profilePic: String,
    val savedMoney: Int,
    val status: String,
    val totalCancelledOrders: Int,
    val totalOrders: Int,
    val updatedAt: String,
    val userNumber: String,
    val userType: String,
    val walletAmount: Int
)