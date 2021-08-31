package com.saveeat.ui.adapter.map

import android.content.Context
import android.graphics.Bitmap
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
    var view: View?=null
    var imageBitmap: Bitmap?=null
    var count=0


    init {
        binding = ClusterViewBinding.inflate(LayoutInflater.from(context),null)
    }

    override fun onBeforeClusterItemRendered(item: ClusterItemAdapter, markerOptions: MarkerOptions) {
        count=0
        binding?.ciLogo?.setImageBitmap(imageBitmap)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context,binding?.root)))
    }





    override fun onClusterItemRendered(clusterItem: ClusterItemAdapter, marker: Marker) {
        count+=1
        Log.e("count", "--->$count")

        marker.tag=clusterItem?.data
        binding?.ciLogo?.setImageBitmap(clusterItem?.data?.bitmap)
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context,binding?.root)))
    }



    override fun onClusterRendered(cluster: Cluster<ClusterItemAdapter>, marker: Marker) {
        val list : MutableList<RestaurantResponseBean?>? = ArrayList()
        val selectedClusterList=ArrayList(cluster.items)
        for(i in selectedClusterList.indices){
            list?.add((ArrayList(cluster.items as MutableCollection<ClusterItemAdapter>)?.get(i) as ClusterItemAdapter).data)
        }
        if(list?.isNullOrEmpty()==false){
            bindingActivity?.rvRestaurant?.visibility=View.VISIBLE
            bindingActivity?.rvRestaurant?.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            bindingActivity?.rvRestaurant?.adapter=MapRestaurantAdapter(context,list)
        }
        if (marker?.tag != null) {
            binding?.ciLogo?.setImageBitmap((marker?.tag as RestaurantResponseBean)?.bitmap)
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context,binding?.root)))
        }

         if (map?.cameraPosition?.zoom?:0f < 14f) {
            bindingActivity.rvRestaurant.visibility=View.GONE
             bindingActivity.btnShowRestro.visibility=View.VISIBLE
        }else{
             bindingActivity.rvRestaurant.visibility=View.VISIBLE
             bindingActivity.btnShowRestro.visibility=View.GONE
        }
    }

    override fun getColor(clusterSize: Int): Int = ContextCompat.getColor(context!!, R.color.app_theme)


}