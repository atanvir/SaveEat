package com.saveeat.model.request.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileModel(var profilePic: String?, var name: String?, var email : String?, var countryCode: String?, var mobileNumber:String?,var token: String?) : Parcelable