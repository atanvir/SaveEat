package com.saveeat.model.response.saveeat.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class DistanceModel(var calculated: Double=0.0) : Parcelable