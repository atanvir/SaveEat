package com.saveeat.repository.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.saveeat.model.request.auth.signup.SignupModel
import com.saveeat.model.response.SaveEat.auth.AuthBean
import com.saveeat.repository.cache.PreferenceKeyConstants._id
import com.saveeat.repository.cache.PreferenceKeyConstants.countryCode
import com.saveeat.repository.cache.PreferenceKeyConstants.distance
import com.saveeat.repository.cache.PreferenceKeyConstants.email
import com.saveeat.repository.cache.PreferenceKeyConstants.jwtToken
import com.saveeat.repository.cache.PreferenceKeyConstants.login
import com.saveeat.repository.cache.PreferenceKeyConstants.longitude
import com.saveeat.repository.cache.PreferenceKeyConstants.mobileNumber
import com.saveeat.repository.cache.PreferenceKeyConstants.name
import com.saveeat.repository.cache.PreferenceKeyConstants.profilePic
import com.saveeat.utils.extn.getData

class DataStore(var context: Context) {

    companion object { val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "cache_data") }

    suspend inline fun <reified T> storeValue(key: Preferences.Key<T>, value: Any) {
        context.dataStore.edit { it[key] = value as T }
    }

    suspend inline fun <reified T> readValue(key: Preferences.Key<T>, crossinline responseFunc: T.() -> Unit) {
        context.dataStore.getData(key) { responseFunc.invoke(this) }
    }

    suspend inline fun <reified T>  storeData(t: T) {
        if(t is AuthBean){
//            storeValue(login,true)
//            storeValue(_id,t._id)
//            storeValue(name,t.name)
//            storeValue(email,t.email?:"")
//            storeValue(profilePic,t.profilePic?:"")
//            storeValue(countryCode,t.countryCode?:"")
//            storeValue(mobileNumber,t.mobileNumber?:"")
//            storeValue(longitude,t.longitude)
//            storeValue(longitude,t.longitude)
//            storeValue(jwtToken,t.jwtToken)
//            storeValue(distance,t.distance?:0)
        }

    }
}
