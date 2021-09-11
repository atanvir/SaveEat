package com.saveeat.model.request.cart

data class AddToCartModel(var restroId: String?, var productId: String?, var quantity: Int?, var choice : MutableList<ChoiceModel?>?, var token : String?, var userId: String?)