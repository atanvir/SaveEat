package com.saveeat.repository.remote.Google

import com.saveeat.model.response.Google.place.GooglePlacesBean
import com.saveeat.repository.remote.Google.GoogleConstant.PLACE_DETAIL
import com.saveeat.utils.application.KeyConstants
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleInterface {

    @GET(PLACE_DETAIL)
    suspend fun gePlaceDetail(@Query("placeid") placeid:String?,
                              @Query("key") key : String= KeyConstants.GOOGLE_PLACE_API) : GooglePlacesBean?
}