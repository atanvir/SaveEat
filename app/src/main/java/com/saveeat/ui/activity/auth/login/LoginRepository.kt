package com.saveeat.ui.activity.auth.login

import com.saveeat.base.BaseRepository
import com.saveeat.model.request.auth.login.LoginModel
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject

class LoginRepository @Inject constructor(private var apiInterface: SaveEatInterface)  : BaseRepository() {

    suspend fun login(model : LoginModel?)= safeApiCall {
        apiInterface.login(model)
    }


}