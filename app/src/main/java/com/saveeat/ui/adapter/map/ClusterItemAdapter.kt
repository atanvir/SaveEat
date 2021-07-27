package com.saveeat.ui.adapter.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class ClusterItemAdapter(var latLng: LatLng, var mTitle: String?, var mSinppet: String?)  : ClusterItem {
    override fun getPosition(): LatLng = latLng
    override fun getTitle(): String? =mTitle
    override fun getSnippet(): String? = mSinppet
}