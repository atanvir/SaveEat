package com.saveeat.model.response.saveeat.menu

import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.bean.ResturantResponse

data class RestaurantObjectModel (var isFav: Boolean,var restroData: RestaurantResponseBean?, var todayData: TodayDataModel?,var distance: Double,var safetyBadge: Boolean)