package com.saveeat.repository.remote.SaveEat

import com.saveeat.model.request.auth.forgot.ForgotModel
import com.saveeat.model.request.auth.login.LoginModel
import com.saveeat.model.request.auth.signup.SignupModel
import com.saveeat.model.request.main.favourite.FavouriteModel
import com.saveeat.model.response.saveeat.auth.AuthModel
import com.saveeat.model.response.saveeat.location.LocationModel
import com.saveeat.model.request.profile.ProfileModel
import com.saveeat.model.request.main.home.CommonHomeModel
import com.saveeat.model.request.menu.MenuItemDetailModel
import com.saveeat.model.request.menu.MenuListModel
import com.saveeat.model.request.menu.MenuModel
import com.saveeat.model.response.saveeat.main.home.HomeModel
import com.saveeat.model.response.saveeat.menu.MenuRestaurantModel
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.ADD_TO_FAVOURITE
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.ALL_FAVOURITE
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.ALL_RESTAURANT
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.CHANGE_MOBILE_NUMBER
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.CHECK_MOBILE_AND_EMAIL
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.CHECK_MOBILE_FORGOT_PASSWORD
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.FORGOT_PASSWORD
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.LOGIN
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.LOGOUT
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.MENU_ITEM_DETAIL
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.MENU_LIST
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.NEW_RESTAURANT_HOME
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.POPULAR_RESTAURANT_HOME
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.RESTAURANT_DETAIL
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.RESTAURANT_HOME
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.SAVE_PRODUCTS_HOME
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
    suspend fun changeMobileNumber(@Body model: ProfileModel?, @Header("Authorization") token: String?) : AuthModel?

    @POST(UPDATE_PROFILE)
    suspend fun userUpdateDetails(@Body model: ProfileModel?, @Header("Authorization") token: String?) : AuthModel?


    @POST(UPDATE_ADDRESS)
    suspend fun updateAddress(@Body model: LocationModel?,@Header("Authorization") token: String?) : AuthModel?


    @GET(LOGOUT)
    suspend fun userLogout(@Header("Authorization") token: String?) : AuthModel?


    @POST(SAVE_PRODUCTS_HOME)
    suspend fun moreSaveProductList(@Body model: CommonHomeModel?, @Header("Authorization") token: String?)  : HomeModel?


    @POST(RESTAURANT_HOME)
    suspend fun restaurantForHomeList(@Body model: CommonHomeModel?, @Header("Authorization") token: String?)  : HomeModel?

    @POST(POPULAR_RESTAURANT_HOME)
    suspend fun popularRestaurantList(@Body model: CommonHomeModel?, @Header("Authorization") token: String?)  : HomeModel?


    @POST(NEW_RESTAURANT_HOME)
    suspend fun newRestaurantList(@Body model: CommonHomeModel?, @Header("Authorization") token: String?)  : HomeModel?


    @FormUrlEncoded
    @POST(ADD_TO_FAVOURITE)
    suspend fun addToFavourite(@Field("storeId") storeId : String?, @Header("Authorization") token: String?)  : HomeModel?


    @POST(ALL_RESTAURANT)
    suspend fun restaurantList(@Body model: CommonHomeModel?, @Header("Authorization") token: String?)  : HomeModel?


    @POST(RESTAURANT_DETAIL)
    suspend fun getRestroDetail(@Body model : MenuModel?,@Header("Authorization") token: String?) : MenuRestaurantModel?

    @POST(MENU_LIST)
    suspend fun getMenuList(@Body model: MenuListModel?,@Header("Authorization") token: String?)  :  MenuRestaurantModel?

    @POST(ALL_FAVOURITE)
    suspend fun getFavoriteRestaurants(@Body model: FavouriteModel?,@Header("Authorization") token: String?)  : HomeModel?


    @POST(MENU_ITEM_DETAIL)
    suspend fun getItemDetail(@Body model: MenuItemDetailModel?,@Header("Authorization") token: String?):  MenuRestaurantModel?

}