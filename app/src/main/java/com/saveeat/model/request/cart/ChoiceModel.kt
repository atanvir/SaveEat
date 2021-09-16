package com.saveeat.model.request.cart

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class ChoiceModel(var categoryId: String?,@SerializedName("_id") var choiceId: String?, @SerializedName("name") var foodName: String?, var price: Double?, var quantity: Int?, var last: Boolean, var check: Boolean) : Parcelable