package com.saveeat.ui.activity.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Handler
import com.saveeat.base.BaseRepository
import com.saveeat.model.request.auth.signup.SignupModel
import com.saveeat.model.response.saveeat.location.LocationModel
import com.saveeat.repository.remote.Google.GoogleInterface
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import com.saveeat.utils.application.KeyConstants.HIDE_INFO_WINDOW
import com.saveeat.utils.application.KeyConstants.NOT_SERVE_THIS_AREA
import com.saveeat.utils.application.Resource
import java.io.IOException
import java.util.*
import javax.inject.Inject

class ChooseAddressRepository  @Inject constructor(private var apiInterface: SaveEatInterface,private var googleInterface: GoogleInterface)  : BaseRepository() {

    suspend fun getCurrentAddres(context: Context?, latitute:Double?, longitute:Double?, handler: Handler?) = safeMethodCall  {
        var addressesList: List<Address>?=null
        try
        {
            addressesList = Geocoder(context, Locale.getDefault()).getFromLocation(latitute?:0.0, longitute?:0.0, 1)
            if(addressesList?.isNotEmpty()!!){
                Resource.Success(addressesList?.get(0)?:"")
                handler?.sendEmptyMessage(HIDE_INFO_WINDOW)
            }else{
                Resource.Success("")
                handler?.sendEmptyMessage(NOT_SERVE_THIS_AREA)
            }
        } catch (e: IOException) {
            Resource.Failure(e)
            handler?.sendEmptyMessage(NOT_SERVE_THIS_AREA)
        }
        return@safeMethodCall addressesList
    }


    suspend fun getPlaceDetail(placeid : String?) = safeApiCall {
        googleInterface.gePlaceDetail(placeid=placeid)
    }

    suspend fun signUp(data: SignupModel?) = safeApiCall {
     apiInterface.signUp(data)
    }

    suspend fun updateAddress(data: LocationModel?,token: String?) = safeApiCall {
        apiInterface.updateAddress(data,token)
    }
}