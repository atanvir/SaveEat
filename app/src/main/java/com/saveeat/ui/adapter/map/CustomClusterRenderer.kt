package com.saveeat.ui.adapter.map

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log

import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

import android.view.LayoutInflater

import android.view.View
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.Cluster

import de.hdodenhof.circleimageview.CircleImageView

import com.google.maps.android.ui.IconGenerator

import com.saveeat.R
import com.saveeat.databinding.ClusterViewBinding
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.utils.application.CommonUtils.createDrawableFromView
import com.saveeat.utils.extn.loadNormalPhoto
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception


class CustomClusterRenderer(var context: Context?, var map: GoogleMap?,var clusterManager: ClusterManager<ClusterItemAdapter?>?) : DefaultClusterRenderer<ClusterItemAdapter>(context,map,clusterManager){
    var binding : ClusterViewBinding?=null

    init {
        binding = ClusterViewBinding.inflate(LayoutInflater.from(context),null)
    }


    override fun onBeforeClusterItemRendered(item: ClusterItemAdapter, markerOptions: MarkerOptions) {
        Picasso.get().load(item?.data?.logo).into(binding?.ciLogo)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context,binding?.root)))
    }

    override fun getColor(clusterSize: Int): Int = ContextCompat.getColor(context!!, R.color.app_theme)


    override fun onClusterItemRendered(clusterItem: ClusterItemAdapter, marker: Marker) {
        marker.tag=clusterItem?.data
        Picasso.get().load(clusterItem?.data?.logo).into(binding?.ciLogo)
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context,binding?.root)))
    }

    override fun onClusterRendered(cluster: Cluster<ClusterItemAdapter>, marker: Marker) {
        if(marker?.tag!=null){
         Picasso.get().load((marker.tag as RestaurantResponseBean)?.logo).into(binding?.ciLogo)
         marker.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context,binding?.root)))
        }
    }

}