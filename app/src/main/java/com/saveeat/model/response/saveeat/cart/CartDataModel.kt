package com.saveeat.model.response.saveeat.cart

import android.media.TimedMetaData
import android.os.Parcel
import android.os.Parcelable
import com.saveeat.model.request.cart.ChoiceModel
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.menu.TodayDataModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartDataModel(var _id: String?,var expanded: Boolean?,var userId: String?,var restroId: String?,var orderType: String?,var pickupDate: String?,var pickupTime: String?,var productData : MutableList<ProductDataModel?>?,var restroData: RestaurantResponseBean?,var timeData: MutableList<TodayDataModel?>?, var choice : MutableList<ChoiceModel?>?) : Parcelable