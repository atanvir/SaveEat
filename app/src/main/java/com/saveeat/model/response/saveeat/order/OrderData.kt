package com.saveeat.model.response.saveeat.order

import com.saveeat.model.request.cart.ChoiceModel
import com.saveeat.model.response.saveeat.cart.ProductDataModel

data class OrderData(
    val _id: String,
    val actualAmount: Double,
    val amountWithQuantuty: Double,
    val choiceAmount: Double,
    val discountAmount: Double,
    var productAmountWithChoice: Double?,
    val extra: List<Any>,
    val mainChoice: MutableList<ChoiceModel?>?,
    val price: Double,
    val productData: ProductDataModel,
    val productId: String,
    val quantity: Int
)