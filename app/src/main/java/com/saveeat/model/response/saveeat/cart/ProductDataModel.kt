package com.saveeat.model.response.saveeat.cart

import android.os.Parcelable
import com.saveeat.model.request.cart.ChoiceModel
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import kotlinx.parcelize.Parcelize


@Parcelize
data class ProductDataModel(var sellingStatus: Boolean?,var type : String?,var quantity: Int?,var choice : MutableList<ChoiceModel?>?,var requirement: String?,var _id: String?,var productId: String?,var foodName: String?,var price: Double?,var offeredPrice: Double?=0.0,var foodImage: String?,var productDetail : RestaurantResponseBean?) : Parcelable
