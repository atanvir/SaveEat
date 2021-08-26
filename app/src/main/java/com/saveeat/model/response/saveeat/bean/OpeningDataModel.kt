package com.saveeat.model.response.saveeat.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OpeningDataModel(
    val _id: String?,
    val day: String?,
    val saleWindowClose: String?,
    val saleWindowOpen: String?
) : Parcelable