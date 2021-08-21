package com.saveeat.model.response.saveeat.menu

import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean

data class MenuItemProductModel(var itemList : MutableList<RestaurantResponseBean?>?,var cuisineId: String?,var cuisineName: String?)