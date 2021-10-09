package com.saveeat.model.request.main.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class GlobalSearchModel(var latitude: String?, var longitude: String?, var distance : String?, var foodType : String?, var limit: Long?,var token : String?,var userId: String?,var search : String?) : Parcelable