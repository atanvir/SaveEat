package com.saveeat.model.response.saveeat.main.search

import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.cart.ProductDataModel

data class SearchBean(var _id: String?,var count: Int?,var productId: String?,var brandId: String?,var productData : CuisinesDataModel?,var search: String?,var mergeData: MutableList<RestaurantResponseBean?>?,var restaurantSearch:MutableList<RestaurantResponseBean?>?,var prouctNameSearch: MutableList<RestaurantResponseBean?>?)