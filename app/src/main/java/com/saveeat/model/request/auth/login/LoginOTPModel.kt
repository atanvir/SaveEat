package com.saveeat.model.request.auth.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginOTPModel(var mobileNumber: String?,var countryCode: String?,var deviceType: String?,var deviceToken: String?) : Parcelable