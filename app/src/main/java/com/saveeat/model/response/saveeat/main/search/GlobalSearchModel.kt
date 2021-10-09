package com.saveeat.model.response.saveeat.main.search

import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.menu.MenuItemProductModel

data class GlobalSearchModel(var status: Int?,var message: String?,var data: SearchBean?)