package com.saveeat.ui.activity.auth.forgot

import com.saveeat.base.BaseRepository
import com.saveeat.model.request.auth.forgot.ForgotModel
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject

class ForgotRepository @Inject constructor(private var apiInterface: SaveEatInterface)  : BaseRepository() {

    suspend fun forgot(model : ForgotModel?)= safeApiCall {
        apiInterface.forgot(model=model)
    }

}