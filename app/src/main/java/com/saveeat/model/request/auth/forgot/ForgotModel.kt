package com.saveeat.model.request.auth.forgot

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForgotModel(var countryCode: String?,var mobileNumber: String?,var userId: String,var password: String?) : Parcelable