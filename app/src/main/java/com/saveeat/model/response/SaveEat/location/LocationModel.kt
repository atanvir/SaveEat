package com.saveeat.model.response.SaveEat.location

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationModel(var address: String?,var latitude: Double?,var longitude: Double?,var distance: String?) : Parcelable