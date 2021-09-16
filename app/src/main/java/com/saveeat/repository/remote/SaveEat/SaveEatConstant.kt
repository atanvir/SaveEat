package com.saveeat.repository.remote.SaveEat

object SaveEatConstant  {
    internal const val SAVEEAT_TESTING_BASE_URL="http://13.234.94.41:3032/api/v1/user/"

    // AUTH
    internal const val CHECK_MOBILE_AND_EMAIL="checkUserMobileAndEmail"
    internal const val SIGNUP="userSignup"
    internal const val LOGIN="userLogin"
    internal const val CHECK_MOBILE_FORGOT_PASSWORD="checkMobilForForgotPassword"
    internal const val FORGOT_PASSWORD="forgotPassword"
    internal const val CHANGE_MOBILE_NUMBER="changeMobileNumber"
    internal const val UPDATE_PROFILE="userUpdateDetails"
    internal const val UPDATE_ADDRESS="updateAddress"
    internal const val LOGOUT="userLogout"
    internal const val SAVE_PRODUCTS_HOME="moreSaveProductList"
    internal const val RESTAURANT_HOME="restaurantForHomeList"
    internal const val POPULAR_RESTAURANT_HOME="popularRestaurantList"
    internal const val NEW_RESTAURANT_HOME="newRestaurantList"
    internal const val ADD_TO_FAVOURITE="addToFavourite"
    internal const val ALL_RESTAURANT="restaurantList"
    internal const val RESTAURANT_DETAIL="getRestroDetail"
    internal const val NEAR_BY_RESTAURANT="nearByRestaurantDetail"
    internal const val MENU_LIST="getMenuList"
    internal const val ALL_FAVOURITE="getFavoriteRestaurants"
    internal const val MENU_ITEM_DETAIL="getItemDetail"
    internal const val ADD_TO_CART="addToCart"
    internal const val UPDATE_CART="updateCart"
    internal const val CART_LIST="getCartItem"
    internal const val REMOVE_CART="deleteItemFromCart"
    internal const val CART_COUNT="getCartCount"
    internal const val ORDER_PLACE="orderItems"
    internal const val ORDER_LIST="getOrderList"
    internal const val ORDER_CANCEL="orderCancelByUser"
    internal const val BADGES="userBadgesEarning"
    internal const val RATING_BY_USER="ratingByUser"
    internal const val CART_COUNT_RATIO="cartCountParticularRestro"

}