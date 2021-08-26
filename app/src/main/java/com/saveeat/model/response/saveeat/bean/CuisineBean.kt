package com.saveeat.model.response.saveeat.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CuisineBean(
    var check: Boolean,
    val _id: String,
    val name: String?
) : Parcelable