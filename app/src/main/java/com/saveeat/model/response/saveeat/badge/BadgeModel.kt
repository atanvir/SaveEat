package com.saveeat.model.response.saveeat.badge

data class BadgeModel(
    val data: BadgeBean?,
    val message: String?,
    val status: Int?,
    var badgeCount: Int?,
)