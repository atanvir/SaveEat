package com.saveeat.ui.fragment.main.location.Map

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.clustering.ClusterManager
import com.saveeat.R
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.ClusterViewBinding
import com.saveeat.databinding.FragmentLocationMapBinding
import com.saveeat.model.request.main.home.CommonHomeModel
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.repository.cache.PrefrencesHelper.getPrefrenceStringValue
import com.saveeat.ui.activity.menu.menu.MenuActivity
import com.saveeat.ui.adapter.map.ClusterItemAdapter
import com.saveeat.ui.adapter.map.CustomClusterRenderer
import com.saveeat.ui.adapter.map.MapRestaurantAdapter
import com.saveeat.ui.fragment.main.location.LocationViewModel
import com.saveeat.utils.application.*
import com.saveeat.utils.application.CustomLoader.Companion.hideLoader
import com.saveeat.utils.application.CustomLoader.Companion.showLoader
import com.saveeat.utils.extn.action
import com.saveeat.utils.extn.snack
import com.saveeat.utils.extn.toast
import com.saveeat.utils.permissions.gps.GPSPermissionHelper
import com.saveeat.utils.permissions.gps.GPSPermissionHelper.startLocation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL


@AndroidEntryPoint
class LocationMapFragment : BaseFragment<FragmentLocationMapBinding>(), OnMapReadyCallback, View.OnClickListener, GoogleMap.OnCameraMoveListener, GPSPermissionHelper.onLocationListner, ClusterManager.OnClusterItemClickListener<ClusterItemAdapter?> {
    private lateinit var mMap: GoogleMap
    private var clusterItemManager: ClusterManager<ClusterItemAdapter?>?=null
    private var requestModel : CommonHomeModel? =null
    private var list : MutableList<RestaurantResponseBean?>? = ArrayList()
    private var cloneList : MutableList<RestaurantResponseBean?>? = ArrayList()
    private val viewModel : LocationViewModel by viewModels()
    private var curLatitude : Double?=0.0
    private var curLongitude : Double?=0.0
    var clusterBinding : ClusterViewBinding?=null
    private var distanceBroadcast: BroadcastReceiver?=null


    private var zoomLevel=14f
    private var isCurrentLocationEnable=false

    private var isSearch=false
    private var searckKey: String?=null


    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLocationMapBinding = FragmentLocationMapBinding.inflate(inflater,container)

    override fun init() {
        clusterBinding = ClusterViewBinding.inflate(LayoutInflater.from(context),null)
        (childFragmentManager?.findFragmentById(R.id.mapView) as SupportMapFragment?)?.getMapAsync(this@LocationMapFragment)
    }

    override fun initCtrl() {
        binding.btnShowRestro.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        requireActivity().registerReceiver(distanceBroadcast, IntentFilter("com.saveeat"))
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(distanceBroadcast)
    }

    override fun observer() {
        lifecycleScope.launch {
            distanceBroadcast = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.getBooleanExtra("update", false)) {
                        isSearch = true
                        searckKey = intent.getStringExtra("search")
//                        showLoader(requireActivity())
                        restaurantApi(latitude = getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.latitude).toDouble(),
                                      longitude = getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.longitude).toDouble()
                        )
                    } else{
                    showLoader(requireActivity())
                    restaurantApi(latitude= getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.latitude).toDouble(),
                                  longitude= getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.longitude).toDouble())}
                }
            }


            viewModel.restaurantList.observe(this@LocationMapFragment,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            CoroutineScope(Dispatchers.Main).launch {
                                withContext(Dispatchers.IO){
                                    try{
                                    clusterItemManager?.clearItems()
                                    list?.clear()
                                    list = it?.value?.data
                                    cloneList = it?.value?.data
                                    val latLngBounds = LatLngBounds.Builder()
                                    for (i in list?.indices!!) {
                                        if(isSearch){
                                            if(list?.get(i)?.businessName?.contains(searckKey?:"",ignoreCase = true)==true){
                                                it.value?.data?.get(i)?.bitmap= BitmapFactory.decodeStream(URL(it.value?.data?.get(i)?.logo).openConnection().getInputStream())
                                                clusterItemManager?.addItem(ClusterItemAdapter(it.value?.data?.get(i)))
                                                latLngBounds.include(LatLng(list?.get(i)?.latitude ?: 0.0, list?.get(i)?.longitude ?: 0.0))
                                            }
                                        }else{
                                            it.value?.data?.get(i)?.bitmap= BitmapFactory.decodeStream(URL(it.value?.data?.get(i)?.logo).openConnection().getInputStream())
                                            clusterItemManager?.addItem(ClusterItemAdapter(it.value?.data?.get(i)))
                                            latLngBounds.include(LatLng(list?.get(i)?.latitude ?: 0.0, list?.get(i)?.longitude ?: 0.0))
                                        }
                                    }
                                    }catch (e: Exception){
                                        e.printStackTrace()
                                    }
                                }
                                withContext(Dispatchers.Main){
                                    hideLoader()
                                    clusterItemManager?.cluster()
                                    binding.rvRestaurant.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
                                    binding.rvRestaurant.adapter=MapRestaurantAdapter(requireActivity(),list,cloneList)

                                    if(isSearch){
                                        isSearch=false
                                        ( binding.rvRestaurant.adapter as MapRestaurantAdapter).filter.filter(searckKey)
                                    }
                                    if(isCurrentLocationEnable){
                                        isCurrentLocationEnable=false
                                        updateCameraPoistion(12f)
                                        binding.btnShowRestro.visibility=View.GONE
                                        binding.rvRestaurant.visibility=View.VISIBLE
                                    }else{
                                        visibleSection()
                                        updateCameraPoistion(zoomLevel)
                                    }
                                }
                            }
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) {
                            hideLoader()
                            ErrorUtil.snackView(binding.root, it.value?.message ?: "")
                        }
                    }
                    is Resource.Failure -> {
                        hideLoader()
                        ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })
        }
    }

    private fun visibleSection() {
        if (mMap?.cameraPosition?.zoom?:0f < 8f) {
            binding.rvRestaurant.visibility=View.GONE
            binding.btnShowRestro.visibility=View.VISIBLE
        }else{
            binding.rvRestaurant.visibility=View.VISIBLE
            binding.btnShowRestro.visibility=View.GONE
        }
    }

    private fun updateCameraPoistion(zoomLevel: Float) {
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.latitude).toDouble(), getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.longitude).toDouble()),zoomLevel))
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.latitude).toDouble(), getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.longitude).toDouble()),zoomLevel))
    }


    override fun onMapReady(p0: GoogleMap) {
        mMap=p0
        updateCameraPoistion(zoomLevel)

        try {
            val success: Boolean = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireActivity(), R.raw.map_style))
        } catch (e: Exception) {
            Log.e("TAG", "Can't find style. Error: ", e)
        }
        mapControls()



        clusterItemManager = ClusterManager(context, mMap)
        mMap.setOnCameraIdleListener(clusterItemManager)
        mMap.setOnMarkerClickListener(clusterItemManager)

        clusterItemManager?.renderer = CustomClusterRenderer(activity, mMap, clusterItemManager, binding)
        mMap.setInfoWindowAdapter(clusterItemManager?.markerManager)
        mMap.setOnInfoWindowClickListener(clusterItemManager)
        mMap.setOnCameraMoveListener(this)

        clusterItemManager?.setOnClusterItemClickListener(this@LocationMapFragment)

        clusterItemManager?.setOnClusterClickListener {
            binding.btnShowRestro.visibility=View.GONE
            binding.rvRestaurant.visibility=View.VISIBLE
            list?.clear()
            val  latLngBounds= LatLngBounds.Builder()
            val selectedClusterList=ArrayList(it.items)
            for(i in selectedClusterList.indices){
                var newList=ArrayList(it.items as MutableCollection<ClusterItemAdapter>)

                list?.add((newList?.get(i) as ClusterItemAdapter).data)
                latLngBounds.include(LatLng(selectedClusterList.get(i)?.data?.latitude?:0.0,selectedClusterList.get(i)?.data?.longitude?:0.0))
            }
            mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLngBounds.build().center))
            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngBounds.build().center, 12f))

            binding.rvRestaurant.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
            binding.rvRestaurant.adapter=MapRestaurantAdapter(requireActivity(),list,cloneList)

            true
        }


        clusterItemManager?.clearItems()

        showLoader(requireActivity())
        restaurantApi(latitude= getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.latitude).toDouble(),
                      longitude= getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.longitude).toDouble())



    }



    private fun mapControls() {
//        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//             mMap?.isMyLocationEnabled = true
//            mMap?.uiSettings?.isMyLocationButtonEnabled = true
//
//        }
        mMap.uiSettings.isCompassEnabled = false
        mMap?.uiSettings?.isScrollGesturesEnabledDuringRotateOrZoom=false
//        mMap?.uiSettings?.isZoomControlsEnabled=true

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

                if(curLatitude==0.0) {
                    startLocation(requireActivity(), onResult, onPermissionLaucher, this)
                }
                else{
                    isCurrentLocationEnable=true
                    showLoader(requireActivity())
                    restaurantApi(curLatitude,curLongitude)
                }
            }
        }
    }

    override fun onCameraMove() {
        visibleSection()
    }


    override fun onLocation(location: Location?) {
        curLatitude=location?.latitude
        curLongitude=location?.longitude
        binding.btnShowRestro.visibility=View.GONE
        showLoader(requireActivity())
        isCurrentLocationEnable=true
        restaurantApi(location?.latitude,location?.longitude)
    }
    private fun restaurantApi(latitude: Double?,longitude: Double?){
        requestModel= CommonHomeModel(latitude= latitude.toString(),
                                      longitude= longitude.toString(),
                                      distance= getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.distance),
                                      foodType = KeyConstants.BOTH,limit = 10,
                                      token = getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.jwtToken) ,
                                      userId= getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants._id))

        viewModel.restaurantList(requestModel)
    }


    override fun onClusterItemClick(item: ClusterItemAdapter?): Boolean {
        val intent= Intent(context, MenuActivity::class.java)
        if(item?.data?.userType?.equals(KeyConstants.BRAND,ignoreCase = true)==true)intent.putExtra("_id",item?.data?._id)
        else intent.putExtra("_id",item?.data?.menuData?._id)
        intent.putExtra("type",item?.data?.userType)
        context?.startActivity(intent)
        return true
    }

    private var onPermissionLaucher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions->
        var permission=true
        permissions.entries.forEach {
            if(!it.value) { permission=it.value }
        }
        when (permission){
            true -> {
                startLocation(requireActivity(), onResultLaucher = onResult, null, this)
            }
            else ->{ binding.root.snack(getString(R.string.turn_on_gps), Snackbar.LENGTH_LONG){
                action(getString(R.string.retry)){
                    startLocation(requireActivity(), onResult, onPermissionLaucher = snackViewPermissionLaucher, this@LocationMapFragment)
                }
            }
            }
        }
    }
    private var snackViewPermissionLaucher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions->
        var permission=true
        permissions.entries.forEach {
            if(!it.value) { permission=it.value }
        }
        when (permission){
            true -> { startLocation(requireActivity(), onResultLaucher = onResult, null, this) }
            else ->{ binding.root.snack(getString(R.string.turn_on_gps), Snackbar.LENGTH_LONG){ requireActivity().toast(getString(R.string.try_again_later))} }
        }
    }
    private var onResult = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
        if(it.resultCode== AppCompatActivity.RESULT_OK){
            showLoader(requireActivity())
            GPSPermissionHelper.loadCurrentLoc()
        }else{
            binding.root.snack(getString(R.string.turn_on_gps)){}
        }
    }



}