package com.saveeat.model.response.saveeat.bean

import android.graphics.Bitmap
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.saveeat.model.response.saveeat.main.home.RestaurantProductModel
import com.saveeat.model.response.saveeat.menu.TodayDataModel
import kotlinx.parcelize.Parcelize


@Parcelize
data class RestaurantResponseBean(
    var _id: String?,
    var userType: String?,
    var addedQuantity: Int?,
    var adminVerifyStatus: String?,
    var avgRating: Double?,
    var loaded: Boolean=false,
    var outOfStock: Boolean?,

    var logo: String?,
    var bitmap: Bitmap?,
    var businessName: String?,
    var dist: DistanceModel?,
    var address: String?,
    var changeRequest: Boolean?,
    var changeRequestApporve: String?,
    var changeRequestId: String?,
    var changeRequestMsg: String?,
    var convertedExpiryDate: String?,
    var createdAt: String?,
    var cuisine: String?,
    var deleteStatus: Boolean?,
    var description: String?,
    var discountAmount: Double?,
    var discountPer: Int?,
    var expiryDate: String?,
    var expiryTime: String?,
    var foodImage: String?,
    var foodName: String?,
    var offeredPrice: Double?,
    var foodQuantity: Int?,
    var safetyBadge: Boolean?,
    var foodType: String?,
    var isChoiceStatus: Boolean?,
    var leftQuantity: Int?,
    var menuCategoryName: String?,
    var menuData: MenuDataModel?,
    var menuId: String?,
    var openingData: OpeningDataModel?,
    var pauseStatus: Boolean?,
    var pickupLaterAllowance: Boolean?,
    var price: Double?,
    var quantitySell: Int?,
    var ratingByUsers: Int?,
    var restroData: RestroData?,
    var saleWindowClose: Int?,
    var saleWindowOpen: Int?,
    var sellingStatus: Boolean?,
    var status: String?,
    var subCategory: String?,
    var totalOrder: Int?,
    var totalRating: Int?,
    var updatedAt: String?,
    var distance: Double?,
    var isFav: Boolean?,
    var leftData: Int?,
    var latitude: Double?,
    var longitude: Double?,
    var maxLeft: MutableList<MaxLeftModel?>?,
    var todayData: TodayDataModel?,
    var image: String?,
    var mobileNumber: String?,
    var webiteLink: String?,
    var cuisineList:  MutableList<CuisineBean?>?,
    var itemData:  MutableList<RestaurantProductModel?>?,
    var realProductData: MutableList<RestaurantProductModel?>?
) : Parcelable