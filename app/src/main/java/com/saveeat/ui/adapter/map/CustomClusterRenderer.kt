package com.saveeat.ui.adapter.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.model.BitmapDescriptor
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
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.lang.Exception


class CustomClusterRenderer(var context: Context?, var map: GoogleMap?, private var clusterManager: ClusterManager<ClusterItemAdapter?>?) : DefaultClusterRenderer<ClusterItemAdapter>(context,map,clusterManager){
    var binding : ClusterViewBinding?=null
    var view: View?=null

    init {
        binding = ClusterViewBinding.inflate(LayoutInflater.from(context),null)
    }

    override fun onBeforeClusterItemRendered(item: ClusterItemAdapter, markerOptions: MarkerOptions) {
        Picasso.get().load(item?.data?.logo).networkPolicy(NetworkPolicy.OFFLINE).resize(50,50).into(binding?.ciLogo)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context,binding?.root)))
    }



    override fun onClusterItemRendered(clusterItem: ClusterItemAdapter, marker: Marker) {
        marker.tag=clusterItem?.data
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            Picasso.get().load(clusterItem?.data?.logo).networkPolicy(NetworkPolicy.OFFLINE).resize(50,50).into(binding?.ciLogo)
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context,binding?.root)))
        },2000)
    }

    override fun getColor(clusterSize: Int): Int = ContextCompat.getColor(context!!, R.color.app_theme)


}