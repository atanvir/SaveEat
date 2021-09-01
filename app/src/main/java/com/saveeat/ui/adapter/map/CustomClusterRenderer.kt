package com.saveeat.ui.adapter.map

import android.content.Context
import android.util.Log

import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

import android.view.LayoutInflater

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.Cluster

import com.saveeat.R
import com.saveeat.databinding.ClusterViewBinding
import com.saveeat.databinding.FragmentLocationMapBinding
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.utils.application.CommonUtils.createDrawableFromView


class CustomClusterRenderer(var context: Context?, var map: GoogleMap?, private var clusterManager: ClusterManager<ClusterItemAdapter?>?, var bindingActivity: FragmentLocationMapBinding) : DefaultClusterRenderer<ClusterItemAdapter>(context,map,clusterManager){
    var binding : ClusterViewBinding?=null

    init {
        binding = ClusterViewBinding.inflate(LayoutInflater.from(context),null)
    }

    override fun onBeforeClusterItemRendered(item: ClusterItemAdapter, markerOptions: MarkerOptions) {
        binding?.ciLogo?.setImageBitmap(item.data?.bitmap)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context,binding?.root)))
    }


    override fun onClusterRendered(cluster: Cluster<ClusterItemAdapter>, marker: Marker) {

        val list : MutableList<RestaurantResponseBean?>? = ArrayList()
        val selectedClusterList=ArrayList(cluster.items)
        for(i in selectedClusterList.indices) list?.add((ArrayList(cluster.items as MutableCollection<ClusterItemAdapter>)?.get(i) as ClusterItemAdapter).data)

        bindingActivity?.rvRestaurant?.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        bindingActivity?.rvRestaurant?.adapter=MapRestaurantAdapter(context,list)

        visibleSection()
    }

    override fun getColor(clusterSize: Int): Int = ContextCompat.getColor(context!!, R.color.app_theme)

    private fun visibleSection() {
        if (map?.cameraPosition?.zoom?:0f < 8f) {
            bindingActivity.rvRestaurant.visibility=View.GONE
            bindingActivity.btnShowRestro.visibility=View.VISIBLE
        }else{
            bindingActivity.rvRestaurant.visibility=View.VISIBLE
            bindingActivity.btnShowRestro.visibility=View.GONE
        }

    }
}