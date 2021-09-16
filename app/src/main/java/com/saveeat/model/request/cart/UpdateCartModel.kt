package com.saveeat.model.request.cart

data class UpdateCartModel(var itemId: String?,var cartId: String?,var quantity: Int?,var choice: MutableList<ChoiceModel?>?,var requirement: String?,var token : String?,var userId: String?,var type : String?)