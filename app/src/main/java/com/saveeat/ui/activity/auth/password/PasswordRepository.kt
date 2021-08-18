package com.saveeat.ui.activity.auth.password

import com.saveeat.base.BaseRepository
import com.saveeat.model.request.auth.forgot.ForgotModel
import com.saveeat.model.request.auth.login.LoginModel
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject

class PasswordRepository @Inject constructor(private var apiInterface: SaveEatInterface)  : BaseRepository() {

    suspend fun forgotPassword(model : ForgotModel?)= safeApiCall {
        apiInterface.forgotPassword(model)
    }


}