package com.saveeat.repository.remote.SaveEat

import com.saveeat.model.request.auth.forgot.ForgotModel
import com.saveeat.model.request.auth.login.LoginModel
import com.saveeat.model.request.auth.signup.SignupModel
import com.saveeat.model.response.SaveEat.auth.AuthModel
import com.saveeat.model.response.SaveEat.location.LocationModel
import com.saveeat.model.request.ProfileModel
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.CHANGE_MOBILE_NUMBER
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.CHECK_MOBILE_AND_EMAIL
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.CHECK_MOBILE_FORGOT_PASSWORD
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.FORGOT_PASSWORD
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.LOGIN
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.LOGOUT
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.SIGNUP
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.UPDATE_ADDRESS
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.UPDATE_PROFILE
import retrofit2.http.*


interface SaveEatInterface {


    // AUTH
    @FormUrlEncoded
    @POST(CHECK_MOBILE_AND_EMAIL)
    suspend fun checkUserMobileAndEmail(@Field ("email") email: String?,
                                        @Field("mobileNumber") mobileNumber: String?) : AuthModel?


    @POST(SIGNUP)
    suspend fun signUp(@Body data: SignupModel?) : AuthModel?

    @POST(LOGIN)
    suspend fun login(@Body data: LoginModel?) : AuthModel?

    @POST(CHECK_MOBILE_FORGOT_PASSWORD)
    suspend fun forgot(@Body model: ForgotModel?) : AuthModel?


    @POST(FORGOT_PASSWORD)
    suspend fun forgotPassword(@Body model: ForgotModel?) : AuthModel?


    @POST(CHANGE_MOBILE_NUMBER)
    suspend fun changeMobileNumber(@Body model: ProfileModel?,@Header("Authorization") token: String?) : AuthModel?

    @POST(UPDATE_PROFILE)
    suspend fun userUpdateDetails(@Body model: ProfileModel?,@Header("Authorization") token: String?) : AuthModel?


    @POST(UPDATE_ADDRESS)
    suspend fun updateAddress(@Body model: LocationModel?,@Header("Authorization") token: String?) : AuthModel?


    @GET(LOGOUT)
    suspend fun userLogout(@Header("Authorization") token: String?) : AuthModel?





}