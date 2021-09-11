package com.saveeat.model.response.saveeat.badge

data class BadgeBean(
    val badgeData: MutableList<BadgeData?>?,
    val rescuseFoodFinal: Double?,
    val savedAmount: Double?,
    val totalRescuedItems: Double?
)