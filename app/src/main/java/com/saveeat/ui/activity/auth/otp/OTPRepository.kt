package com.saveeat.ui.activity.auth.otp

import com.saveeat.base.BaseRepository
import com.saveeat.model.request.auth.login.LoginModel
import com.saveeat.model.request.auth.login.LoginOTPModel
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject

class OTPRepository @Inject constructor(private var apiInterface: SaveEatInterface)  : BaseRepository() {


    suspend fun loginByOtp(model : LoginOTPModel?)= safeApiCall {
        apiInterface.loginByOtp(model=model)
    }


}