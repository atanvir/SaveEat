package com.saveeat.model.response.saveeat.menu

import android.os.Parcelable
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuItemProductModel(var itemList : MutableList<RestaurantResponseBean?>?,var cuisineId: String?,var cuisineName: String?) : Parcelable