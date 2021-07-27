package com.saveeat.model.request.restaurant

import com.saveeat.model.request.brand.BrandModel

data class CategoryModel(var isFavScreen: Boolean?, var superRestro : Boolean?, var outOfStock:Boolean?, var sectionName : String, var sectionImage: Int?, var image : Int?, var name : String?, var rating: Int?, var distance : Double?, var fav : Boolean?, var products : MutableList<RestaurantProductModel?>?,var brand: MutableList<BrandModel?>?,var save : MutableList<SaveRestaurantsModel?>?)