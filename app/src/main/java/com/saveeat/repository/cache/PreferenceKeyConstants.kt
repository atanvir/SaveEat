package com.saveeat.repository.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object PreferenceKeyConstants {
    const val deviceToken="deviceToken"
    const val _id= "_id"
    const val countryCode= "countryCode"
    const val address= "address"
    const val name= "name"
    const val latitude= "latitude"
    const val profilePic= "profilePic"
    const val longitude= "longitude"
    const val distance= "distance"
    const val jwtToken= "jwtToken"
    const val mobileNumber= "mobileNumber"
    const val email= "email"
    const val walkthrow : String="walkthrow"
    const val login : String="login"

    const val about_us : String="About Us"
    const val term_condition : String="Terms and Conditions"
    const val privacy_policy : String="Privacy Policy"
    const val refund_cancellation : String="Refund and Cancellation"

}