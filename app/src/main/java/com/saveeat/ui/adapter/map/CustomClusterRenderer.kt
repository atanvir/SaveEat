package com.saveeat.ui.adapter.map

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

import android.view.LayoutInflater

import android.view.View
import android.widget.ProgressBar

import com.google.android.gms.maps.model.Marker

import de.hdodenhof.circleimageview.CircleImageView

import com.google.maps.android.ui.IconGenerator
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.maps.android.clustering.Cluster
import android.widget.ImageView
import com.saveeat.utils.application.CommonUtils
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import android.widget.RelativeLayout


class CustomClusterRenderer(var context: Context?, var map: GoogleMap?,var clusterManager: ClusterManager<ClusterItemAdapter?>?) : DefaultClusterRenderer<ClusterItemAdapter>(context,map,clusterManager){
    private var ciClusterLogo : CircleImageView?=null
    private var clusterProgressBar : ProgressBar?=null

    private var clusterGenerator: IconGenerator? = null
    private var markerGenerator: IconGenerator? = null

    init {
        setUpClusterIcon()
        setUpMarker()
    }

    override fun onBeforeClusterItemRendered(item: ClusterItemAdapter, markerOptions: MarkerOptions) {
        val icon = clusterGenerator?.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
        icon?.recycle()
    }

    private fun setUpClusterIcon() {
        val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(45, 45)
        val marker = ImageView(context!!)
        marker.layoutParams = params
        clusterGenerator = IconGenerator(context!!)
        clusterGenerator?.setContentView(marker)
        clusterGenerator?.setBackground(null)
    }

    private fun setUpMarker() {
        markerGenerator = IconGenerator(context!!)
        val markerView : View = LayoutInflater.from(context).inflate(com.saveeat.R.layout.cluster_view, null)
        ciClusterLogo=markerView.findViewById(com.saveeat.R.id.ciLogo)
        clusterProgressBar=markerView.findViewById(com.saveeat.R.id.progressBar)
        markerGenerator?.setContentView(markerView)
        markerGenerator?.setBackground(null)
    }

    override fun onClusterRendered(cluster: Cluster<ClusterItemAdapter>, marker: Marker) {
        super.onClusterRendered(cluster, marker)

        Glide.with(context!!).load((marker.tag as RestaurantResponseBean).image).listener(object : RequestListener<Drawable>{
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                clusterProgressBar?.visibility=View.GONE
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                clusterProgressBar?.visibility=View.GONE
                return false
            }

        }).into(ciClusterLogo!!)
    }



    override fun getColor(clusterSize: Int): Int = ContextCompat.getColor(context!!, com.saveeat.R.color.app_theme)

    override fun onClusterItemRendered(clusterItem: ClusterItemAdapter, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)
        marker.tag=clusterItem.data
        Glide.with(context!!).load(clusterItem.data?.image).listener(object : RequestListener<Drawable>{
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                clusterProgressBar?.visibility=View.GONE
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(CommonUtils.convertToBitmap(resource,80,80)))
                clusterProgressBar?.visibility=View.GONE
                return false
            }

        }).into(ciClusterLogo!!)
    }



}