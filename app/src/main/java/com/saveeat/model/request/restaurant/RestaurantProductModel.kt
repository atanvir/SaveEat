package com.saveeat.model.request.restaurant

data class RestaurantProductModel(var stock : String?, var outOfStock : Boolean?, var veg : Boolean?, var sellingPrice : Double?, var marketPrice : Double?, var image : Int?, var name : String?)