package com.saveeat.repository.cache

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.saveeat.model.response.saveeat.auth.AuthBean
import com.saveeat.model.response.saveeat.location.LocationModel
import com.saveeat.repository.cache.PreferenceKeyConstants._id
import com.saveeat.repository.cache.PreferenceKeyConstants.address
import com.saveeat.repository.cache.PreferenceKeyConstants.countryCode
import com.saveeat.repository.cache.PreferenceKeyConstants.distance
import com.saveeat.repository.cache.PreferenceKeyConstants.email
import com.saveeat.repository.cache.PreferenceKeyConstants.jwtToken
import com.saveeat.repository.cache.PreferenceKeyConstants.latitude
import com.saveeat.repository.cache.PreferenceKeyConstants.login
import com.saveeat.repository.cache.PreferenceKeyConstants.longitude
import com.saveeat.repository.cache.PreferenceKeyConstants.mobileNumber
import com.saveeat.repository.cache.PreferenceKeyConstants.name
import com.saveeat.repository.cache.PreferenceKeyConstants.profilePic
import com.saveeat.repository.cache.PreferenceKeyConstants.walkthrow
import com.saveeat.utils.application.KeyConstants.PREF_NAME

object PrefrencesHelper {

    fun getPrefrenceStringValue(context: Context, KEY: String): String{
        return  context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(KEY, "").toString()
    }

    fun getPrefrenceBooleanValue(context: Context, KEY: String) : Boolean{
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getBoolean(KEY, false)
    }


    fun writePrefrencesValue(context: Context): SharedPreferences.Editor{
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
    }

    fun saveUserData(context: Context,data: AuthBean?){
        writePrefrencesValue(context).apply {
            putBoolean(login,true)
            putString(_id,data?._id)
            putString(name,data?.name)
            putString(email,data?.email?:"")
            putString(address,data?.address?:"")
            putString(profilePic,data?.profilePic?:"")
            putString(countryCode,data?.countryCode?:"")
            putString(mobileNumber,data?.mobileNumber?:"")
            putString(longitude,data?.longitude)
            putString(latitude,data?.latitude)
            putString(jwtToken,data?.jwtToken)
            putString(distance,data?.distance.toString())
        }.apply()
    }

    fun updateUserData(context: Context, data: AuthBean?) {
        writePrefrencesValue(context).apply {
            putString(name,data?.name)
            putString(email,data?.email?:"")
            putString(profilePic,data?.profilePic?:"")
            putString(countryCode,data?.countryCode?:"")
            putString(mobileNumber,data?.mobileNumber?:"")
        }.apply()
    }

    fun userLogout(context: Context) {
      context?.getSharedPreferences(PREF_NAME,MODE_PRIVATE)?.edit()?.clear()?.apply()
      context?.getSharedPreferences(PREF_NAME,MODE_PRIVATE)?.edit()?.putBoolean(walkthrow,true)?.apply()
    }

    fun updateLocation(context: Context,location: LocationModel?) {
        writePrefrencesValue(context).apply {
            putString(distance,location?.distance)
            putString(address,location?.address)
            putString(latitude,location?.latitude.toString()?:"")
            putString(longitude,location?.longitude.toString()?:"")
        }.apply()
    }
}