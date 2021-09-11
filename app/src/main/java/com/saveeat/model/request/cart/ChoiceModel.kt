package com.saveeat.model.request.cart

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ChoiceModel(var id: String?,var foodName: String?,var price: Double?,var quantity: Int?) : Parcelable