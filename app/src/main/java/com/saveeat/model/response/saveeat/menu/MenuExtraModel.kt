package com.saveeat.model.response.saveeat.menu

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuExtraModel(
    val _id: String?,
    val cuisine: String?,
    val description: String?,
    val foodImage: String?,
    val foodName: String?,
    val foodQuantity: Int?,
    val foodType: String?,
    val price: Double?,
    var check: Boolean=false,
    val subCategory: String?
) : Parcelable