package com.saveeat.model.request.auth.signup

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignupModel(var password: String?,var name:String?,var email: String?,var countryCode: String?,var mobileNumber: String?,var deviceType: String?,var deviceToken: String,var address: String,var latitude: Double?,var longitude: Double?,var distance: String?) : Parcelable