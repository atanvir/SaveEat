package com.saveeat.model.response.saveeat.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RestroData(
    val _id: String?,
    val businessName: String?,
    val dist: DistanceModel?,
    val foodType: String?,
    val image: String?,
    val latitude: String?,
    val logo: String?,
    val longitude: String?,
    val userType: String?,
    val avgRating: Double?,
    val safetyBadge : Boolean?
) : Parcelable