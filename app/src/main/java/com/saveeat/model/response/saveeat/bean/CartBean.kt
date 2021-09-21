package com.saveeat.model.response.saveeat.bean

import com.saveeat.model.response.saveeat.cart.CartDataModel
import com.saveeat.model.response.saveeat.cart.TaxCommissionModel

data class CartBean(var cartData: MutableList<CartDataModel?>?,var taxCommission: TaxCommissionModel?)