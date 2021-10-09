package com.saveeat.repository.remote.SaveEat

import com.saveeat.model.request.auth.forgot.ForgotModel
import com.saveeat.model.request.auth.login.LoginModel
import com.saveeat.model.request.auth.login.LoginOTPModel
import com.saveeat.model.request.auth.signup.SignupModel
import com.saveeat.model.request.cart.CartCount
import com.saveeat.model.request.cart.CartRequestCount
import com.saveeat.model.request.cart.CartRequestModel
import com.saveeat.model.request.cart.UpdateCartModel
import com.saveeat.model.request.filter.FilterRequestModel
import com.saveeat.model.request.filter.SubCategoryModel
import com.saveeat.model.response.saveeat.auth.AuthModel
import com.saveeat.model.response.saveeat.location.LocationModel
import com.saveeat.model.request.profile.ProfileModel
import com.saveeat.model.request.main.home.CommonHomeModel
import com.saveeat.model.request.main.search.GlobalSearchModel
import com.saveeat.model.request.main.search.SaveRecentSearch
import com.saveeat.model.request.menu.MenuBrandModel
import com.saveeat.model.request.menu.MenuItemDetailModel
import com.saveeat.model.request.menu.MenuListModel
import com.saveeat.model.request.menu.MenuModel
import com.saveeat.model.request.order.OrderCancelModel
import com.saveeat.model.request.order.OrderHistoryModel
import com.saveeat.model.request.order.OrderPlaceModel
import com.saveeat.model.request.rating.RatingModel
import com.saveeat.model.response.saveeat.badge.BadgeModel
import com.saveeat.model.response.saveeat.cart.CartModel
import com.saveeat.model.response.saveeat.cart.DeleteItemCart
import com.saveeat.model.response.saveeat.main.filter.FilterCuisinesModel
import com.saveeat.model.response.saveeat.main.filter.FilterSubCategoryModel
import com.saveeat.model.response.saveeat.main.home.HomeModel
import com.saveeat.model.response.saveeat.main.search.SearchModel
import com.saveeat.model.response.saveeat.menu.MenuRestaurantModel
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.ADD_TO_CART
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.ADD_TO_FAVOURITE
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.ALL_FAVOURITE
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.ALL_RESTAURANT
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.BADGES
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.CART_COUNT
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.CART_COUNT_RATIO
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.CART_LIST
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.CHANGE_MOBILE_NUMBER
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.CHECK_MOBILE_AND_EMAIL
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.CHECK_MOBILE_FORGOT_PASSWORD
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.CUISINE_LIST
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.CUISINE_SUB_CATEGORY
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.FILTER
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.FORGOT_PASSWORD
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.GLOBAL_SEARCH
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.LOGIN
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.LOGIN_BY_OTP
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.LOGOUT
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.MENU_ITEM_DETAIL
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.MENU_LIST
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.MENU_LIST_FILTER
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.NEAR_BY_RESTAURANT
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.NEW_RESTAURANT_HOME
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.ORDER_CANCEL
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.ORDER_LIST
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.ORDER_PLACE
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.POPULAR_CUISINES
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.POPULAR_RESTAURANT_HOME
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.RATING_BY_USER
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.RECENT_SEARCH
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.REMOVE_CART
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.RESTAURANT_DETAIL
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.RESTAURANT_HOME
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.SAVE_PRODUCTS_HOME
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.SAVE_RECENT_SEARCH
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.SEARCH_SUGGESTION
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.SIGNUP
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.UPDATE_ADDRESS
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.UPDATE_CART
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.UPDATE_PROFILE
import retrofit2.http.*


interface SaveEatInterface {

    @FormUrlEncoded
    @POST(CHECK_MOBILE_AND_EMAIL)
    suspend fun checkUserMobileAndEmail(@Field ("email") email: String?,
                                        @Field("mobileNumber") mobileNumber: String?) : AuthModel?


    @POST(SIGNUP)
    suspend fun signUp(@Body data: SignupModel?) : AuthModel?

    @POST(LOGIN)
    suspend fun login(@Body data: LoginModel?) : AuthModel?

    @POST(CHECK_MOBILE_FORGOT_PASSWORD)
    suspend fun forgot(@Body model: Any?) : AuthModel?


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

    @POST(MENU_LIST_FILTER)
    suspend fun getMenuListByFilter(@Body model: FilterRequestModel?,@Header("Authorization") token: String?)  :  MenuRestaurantModel?


    @POST(NEAR_BY_RESTAURANT)
    suspend fun nearByRestaurantDetail(@Body model: MenuBrandModel?, @Header("Authorization") token: String?)  :  MenuRestaurantModel?

    @POST(ALL_FAVOURITE)
    suspend fun getFavoriteRestaurants(@Body model: CommonHomeModel?,@Header("Authorization") token: String?)  : HomeModel?


    @POST(MENU_ITEM_DETAIL)
    suspend fun getItemDetail(@Body model: MenuItemDetailModel?,@Header("Authorization") token: String?):  MenuRestaurantModel?


    @POST(ADD_TO_CART)
    suspend fun addToCart(@Body modelAddTo: com.saveeat.model.request.cart.AddToCartModel?, @Header("Authorization") token: String?) : CartModel?


    @POST(UPDATE_CART)
    suspend fun updateCart(@Body modelAddTo: UpdateCartModel?, @Header("Authorization") token: String?) : CartModel?

    @POST(CART_LIST)
    suspend fun cartList(@Body model: CartRequestModel?, @Header("Authorization") token: String?) : CartModel?

    @POST(REMOVE_CART)
    suspend fun deleteItemFromCart(@Body model: DeleteItemCart?, @Header("Authorization") token: String?)  : CartModel?

    @GET(CART_COUNT)
    suspend fun getCartCount(@Header("Authorization") token: String?) : CartCount?

    @POST(ORDER_PLACE)
    suspend fun orderItems(@Body model: OrderPlaceModel?, @Header("Authorization") token: String?) : CartModel?

    @POST(ORDER_LIST)
    suspend fun getOrderList(@Body model: OrderHistoryModel?,  @Header("Authorization") token: String?) : com.saveeat.model.response.saveeat.order.OrderHistoryModel?

    @POST(ORDER_CANCEL)
    suspend fun orderCancelByUser(@Body model: OrderCancelModel?,  @Header("Authorization") token: String?) : com.saveeat.model.response.saveeat.order.OrderHistoryModel?

    @GET(BADGES)
    suspend fun userBadgesEarning(@Header("Authorization") token: String?) : BadgeModel?

    @POST(RATING_BY_USER)
    suspend fun ratingByUser(@Body model: RatingModel?,  @Header("Authorization") token: String?) : com.saveeat.model.response.saveeat.order.OrderHistoryModel?

    @POST(CART_COUNT_RATIO)
    suspend fun cartCountParticularRestro(@Body model: CartRequestCount?, @Header("Authorization") token: String?) :CartCount?

    @POST(LOGIN_BY_OTP)
    suspend fun loginByOtp(@Body model: LoginOTPModel?) : AuthModel?


    @POST(POPULAR_CUISINES)
    suspend fun popularCuisineProducts(@Body model: CommonHomeModel?,  @Header("Authorization") token: String?) : SearchModel?



    @POST(RECENT_SEARCH)
    suspend fun getRecentSearch(@Body model: CommonHomeModel?,  @Header("Authorization") token: String?) : SearchModel?


    @POST(SEARCH_SUGGESTION)
    suspend fun searchSuggestions(@Body model: GlobalSearchModel?,  @Header("Authorization") token: String?) : com.saveeat.model.response.saveeat.main.search.GlobalSearchModel?


    @POST(GLOBAL_SEARCH)
    suspend fun globalSearch(@Body model: GlobalSearchModel?,  @Header("Authorization") token: String?) : com.saveeat.model.response.saveeat.main.search.GlobalSearchModel?


    @POST(SAVE_RECENT_SEARCH)
    suspend fun saveRecentSearch(@Body model: SaveRecentSearch?) : SearchModel?

    @GET(CUISINE_LIST)
    suspend fun cuisineList() : FilterCuisinesModel?

    @POST(CUISINE_SUB_CATEGORY)
    suspend fun cuisineSubCategory(@Body model: SubCategoryModel?, @Header("Authorization") token: String?) : FilterCuisinesModel?


    @POST(FILTER)
    suspend fun restaurantFilter(@Body model: FilterRequestModel?, @Header("Authorization") token: String?) : HomeModel?


}