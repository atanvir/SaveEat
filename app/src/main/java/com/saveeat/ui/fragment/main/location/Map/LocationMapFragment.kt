package com.saveeat.ui.fragment.main.location.Map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterManager
import com.saveeat.R
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.FragmentLocationMapBinding
import com.saveeat.ui.adapter.map.ClusterItemAdapter
import com.saveeat.ui.adapter.map.CustomClusterRenderer
import com.saveeat.ui.adapter.map.MapRestaurantAdapter
import com.saveeat.utils.application.StaticDataHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class LocationMapFragment : BaseFragment<FragmentLocationMapBinding>(), OnMapReadyCallback, View.OnClickListener {
    private lateinit var mMap: GoogleMap
    private var clusterItemManager: ClusterManager<ClusterItemAdapter?>?=null

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLocationMapBinding = FragmentLocationMapBinding.inflate(inflater,container)

    override fun init() {
        (childFragmentManager?.findFragmentById(R.id.mapView) as SupportMapFragment?)?.getMapAsync(this)
    }

    override fun initCtrl() {
        binding.btnShowRestro.setOnClickListener(this)
    }

    override fun observer() {
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap=p0
        mapControls()

        val currentLocation=LatLng(28.6259,77.3774)


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLocation.latitude, currentLocation.longitude), 7f))
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 7f))
        clusterItemManager = ClusterManager(context, mMap)
        mMap.setOnCameraIdleListener(clusterItemManager)
        mMap.setOnMarkerClickListener(clusterItemManager)

        clusterItemManager?.renderer = CustomClusterRenderer(activity, mMap, clusterItemManager)

        mMap.setInfoWindowAdapter(clusterItemManager?.markerManager)
        mMap.setOnInfoWindowClickListener(clusterItemManager)

        clusterItemManager?.setOnClusterClickListener {
            binding.btnShowRestro.visibility=View.GONE
            binding.rvRestaurant.visibility=View.VISIBLE
            val  latLngBounds= LatLngBounds.Builder()
            val selectedClusterList=ArrayList(it.items)
            for(i in selectedClusterList.indices){
                latLngBounds.include(selectedClusterList.get(i)?.latLng)
            }


            mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLngBounds.build().center))
            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngBounds.build().center, 10f))
            true
        }

        addItems()
        binding.rvRestaurant.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        binding.rvRestaurant.adapter=MapRestaurantAdapter(requireActivity(),StaticDataHelper.getMapRestaurant())


    }

    private fun addItems() {

        val currentLocation=LatLng(28.6259,77.3774)
        var lat = currentLocation.latitude
        var lng = currentLocation.longitude

        for (i in 0..9) {
            val offset = i / 60.0
            lat += offset
            lng += offset
            var offsetItem : ClusterItemAdapter?=ClusterItemAdapter(LatLng(lat,lng), "Title $i", "Snippet $i")
            clusterItemManager?.addItem(offsetItem)
        }
    }


    private fun mapControls() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
             mMap?.isMyLocationEnabled = true
            mMap?.uiSettings?.isMyLocationButtonEnabled = true

        }
        mMap?.uiSettings?.isScrollGesturesEnabledDuringRotateOrZoom=false
        mMap?.uiSettings?.isZoomControlsEnabled=true

        // Location Button Gravity
        val btnMyLocation = (childFragmentManager?.findFragmentById(R.id.mapView)?.view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<View>(Integer.parseInt("2"))
        val params = RelativeLayout.LayoutParams( btnMyLocation.layoutParams.width, btnMyLocation.layoutParams.height) // size of button in dp
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
        params.setMargins(0, 0, 25, 0)
        btnMyLocation.layoutParams=params

        // Zoom Button
        mMap?.setPadding(0, 0, 0, 650)
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnShowRestro->{
                val currentLocation=LatLng(28.6259,77.3774)

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLocation.latitude, currentLocation.longitude), 21f))
                mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 21f))

                binding.btnShowRestro.visibility=View.GONE
                 binding.rvRestaurant.visibility=View.VISIBLE
            }
        }
    }


}