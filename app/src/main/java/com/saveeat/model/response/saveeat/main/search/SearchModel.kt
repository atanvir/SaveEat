package com.saveeat.model.response.saveeat.main.search

import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean


data class SearchModel(var status: Int?,var message: String?,var data : MutableList<SearchBean?>?)
