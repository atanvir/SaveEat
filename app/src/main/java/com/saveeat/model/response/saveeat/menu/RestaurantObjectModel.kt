package com.saveeat.model.response.saveeat.menu

import android.os.Parcelable
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantObjectModel (var isFav: Boolean?,var restroData: RestaurantResponseBean?, var todayData: TodayDataModel?,var distance: Double=0.0,var safetyBadge: Boolean?) : Parcelable