package com.saveeat.model.response.saveeat.badge

data class BadgeData(
    val _id: String,
    val badgeCount: Int,
    val image: String,
    val message: String,
    val name: String,
    val greyContent: String,
    val fullColorContent: String,
    val emojiGrey: String,
    val emojiColor: String,
)