package com.saveeat.ui.adapter.map

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.saveeat.R


class CustomClusterRenderer(var context: Context?, var map: GoogleMap?,var clusterManager: ClusterManager<ClusterItemAdapter?>?) : DefaultClusterRenderer<ClusterItemAdapter>(context,map,clusterManager){

    var count : Int=0
    override fun onBeforeClusterItemRendered(item: ClusterItemAdapter, markerOptions: MarkerOptions) {
        var descriptor : BitmapDescriptor?=null
        descriptor = if(count%2==0) BitmapDescriptorFactory.fromResource(R.drawable.group_4002)
        else BitmapDescriptorFactory.fromResource(R.drawable.group_4001)
        count += 1
        markerOptions.icon(descriptor)

    }


    override fun getColor(clusterSize: Int): Int = ContextCompat.getColor(context!!,R.color.app_theme)


}