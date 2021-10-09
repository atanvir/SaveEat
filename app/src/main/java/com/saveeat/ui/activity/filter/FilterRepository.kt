package com.saveeat.ui.activity.filter

import com.saveeat.base.BaseRepository
import com.saveeat.model.request.filter.FilterRequestModel
import com.saveeat.model.request.filter.SubCategoryModel
import com.saveeat.model.request.rating.RatingModel
import com.saveeat.model.response.saveeat.bean.CuisineBean
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject


class FilterRepository @Inject constructor(private var apiInterface: SaveEatInterface)  : BaseRepository() {

    suspend fun cuisineList()= safeApiCall {
        apiInterface.cuisineList()
    }

    suspend fun cuisineSubCategory(model: SubCategoryModel?)= safeApiCall {
        apiInterface.cuisineSubCategory(model,model?.token)
    }

    suspend fun restaurantFilter(model: FilterRequestModel?)= safeApiCall {
        apiInterface.restaurantFilter(model,model?.token)
    }
}
