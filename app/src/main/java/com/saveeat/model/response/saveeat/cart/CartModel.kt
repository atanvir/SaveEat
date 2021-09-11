package com.saveeat.model.response.saveeat.cart

import com.saveeat.model.response.saveeat.bean.CartBean

data class CartModel(var status: Int?,var message: String,var cartData: CartDataModel?,var data: CartBean?)