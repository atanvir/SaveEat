package com.saveeat.model.response.saveeat.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MenuDataModel(
    val __v: Int?,
    val _id: String?,
    val adminVerificationStatus: String?,
    val brandId: String?,
    val createdAt: String?,
    val deleteStatus: Boolean?,
    val menuName: String?,
    val status: String?,
    val updatedAt: String?
) : Parcelable