package com.saveeat.ui.adapter.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean

class ClusterItemAdapter(var data : RestaurantResponseBean?)  : ClusterItem {
    override fun getPosition(): LatLng = LatLng(data?.latitude?:0.0,data?.longitude?:0.0)
    override fun getTitle(): String? =data?.businessName
    override fun getSnippet(): String? = data?.address
}