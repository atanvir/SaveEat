package com.saveeat.model.response.saveeat.menu

import android.os.Parcelable
import com.saveeat.model.request.cart.ChoiceModel
import kotlinx.parcelize.Parcelize


@Parcelize
class MenuCategoryModel(var  count: Long?,var _id: String?,var categoryName : String?,var error: Boolean=false,var min : Int?,var max: Int?,var choice: MutableList<ChoiceModel?>?) : Parcelable