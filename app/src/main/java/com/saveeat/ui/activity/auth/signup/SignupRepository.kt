package com.saveeat.ui.activity.auth.signup

import com.saveeat.base.BaseRepository
import com.saveeat.repository.remote.Google.GoogleInterface
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject

class SignupRepository @Inject constructor(private var apiInterface: SaveEatInterface)  : BaseRepository() {

    suspend fun checkUserMobileAndEmail(email: String?,mobileNumber: String?)= safeApiCall {
        apiInterface.checkUserMobileAndEmail(email=email,mobileNumber=mobileNumber)
    }


}