package com.saveeat.model.response.saveeat.menu

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class TodayDataModel(
    val _id: String?,
    val day: String?,
    val pickupWindowClose: String?,
    val pickupWindowOpen: String?,
    val pickupOpenTime: String?,
    val pickupCloseTime: String?,
    val saleWindowClose: String?,
    val saleWindowOpen: String?
) : Parcelable