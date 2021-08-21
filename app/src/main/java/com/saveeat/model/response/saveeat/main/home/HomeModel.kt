package com.saveeat.model.response.saveeat.main.home

import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean

data class HomeModel(var status: Int?,var message: String?,var data : MutableList<RestaurantResponseBean?>?)