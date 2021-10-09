package com.saveeat.model.request.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterRequestModel(
    val cuisines: List<String>,
    val distance: String,
    val foodType: String,
    val latitude: Double,
    val longitude: Double,
    val maxPrice: Int,
    val minPrice: Int,
    val pickupLaterAllowance: Boolean,
    val rating: Int,
    val subCategory: List<String>,
    val token: String,
    val userId: String,
    var menuId: String,
) : Parcelable