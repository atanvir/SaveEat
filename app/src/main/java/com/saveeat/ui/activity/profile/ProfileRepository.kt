package com.saveeat.ui.activity.profile

import com.saveeat.base.BaseRepository
import com.saveeat.model.request.profile.ProfileModel
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject

class ProfileRepository @Inject constructor(private var apiInterface: SaveEatInterface)  : BaseRepository() {

    suspend fun changeMobileNumber(model : ProfileModel?)= safeApiCall {
        apiInterface.changeMobileNumber(model,model?.token)
    }

    suspend fun userUpdateDetails(model : ProfileModel?)= safeApiCall {
        apiInterface.userUpdateDetails(model,model?.token)
    }


}