package com.saveeat.model.response.saveeat.menu

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.saveeat.model.response.saveeat.bean.CuisineBean
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.main.home.RestaurantProductModel
import com.saveeat.model.response.saveeat.menu.TodayDataModel
import kotlinx.parcelize.Parcelize
@Parcelize
data class RestaurantDetailModel(
    var _id: String?,
    var avgRating : String?,
    var foodName: String?,
    var description: String?,
    var foodType: String?,
    var foodImage: String?,
    var menuId: String?,
    var logo: String?,
    var businessName: String?,
    var address: String?,
    var createdAt: String?,
    var menuData: String?,
    var pickupLaterAllowance: Boolean?,
    var price: Double?,
    var offeredPrice: Double?,
    var quantitySell: Int?,
    var ratingByUsers: Int?,
    var saleWindowClose: Int?,
    var saleWindowOpen: Int?,
    var sellingStatus: Boolean?,
    var status: String?,
    var subCategory: String?,
    var totalOrder: Int?,
    var totalRating: Int?,
    var updatedAt: String?,
    var restroObj: RestaurantObjectModel?,
    var distance: Double=0.0,
    var isFav: Boolean?,
    var leftData: Int?,
    var latitude: Double?,
    var longitude: Double?,
    var todayData: TodayDataModel?,
    var extra: MutableList<MenuExtraModel?>?,
    var image: String?,
    var menu: Boolean=false,
    var restroData: RestaurantResponseBean?,
    var cuisineList:  MutableList<CuisineBean?>?,
    var itemData:  MutableList<MenuItemProductModel?>?,
    var itemListAll:  MutableList<RestaurantResponseBean?>?,
    var realProductData: MutableList<RestaurantProductModel?>?
) : Parcelable