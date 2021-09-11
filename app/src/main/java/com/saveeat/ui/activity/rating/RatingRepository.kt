package com.saveeat.ui.activity.rating

import com.saveeat.base.BaseRepository
import com.saveeat.model.request.profile.ProfileModel
import com.saveeat.model.request.rating.RatingModel
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject

class RatingRepository @Inject constructor(private var apiInterface: SaveEatInterface)  : BaseRepository() {

    suspend fun ratingByUser(model : RatingModel?)= safeApiCall {
        apiInterface.ratingByUser(model, model?.token)
    }
}