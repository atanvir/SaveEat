package com.saveeat.ui.activity.location

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.biteMe.ui.adapter.autocomplete.onAutoCompleteItemClick
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityChooseAddressBinding
import com.saveeat.model.request.address.PlacesModel
import com.saveeat.ui.activity.main.MainActivity
import com.saveeat.ui.adapter.address.AddressInfoWindow
import com.saveeat.ui.adapter.autocomplete.AutoCompleteAddressAdapter
import com.saveeat.utils.application.Resource
import com.saveeat.utils.extn.*
import com.saveeat.utils.permissions.gps.GPSPermissionHelper
import com.saveeat.utils.permissions.gps.GPSPermissionHelper.loadCurrentLoc
import com.saveeat.utils.permissions.gps.GPSPermissionHelper.startLocation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class ChooseAddressActivity : BaseActivity<ActivityChooseAddressBinding>(), GPSPermissionHelper.onLocationListner, OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener, View.OnClickListener, onAutoCompleteItemClick, SearchView.OnCloseListener {
    private val GETTING_ADDRESS = 1
    private val NOT_SERVE_THIS_AREA = 2
    private val HIDE_INFO_WINDOW = 3
    private var list: MutableList<PlacesModel?>? = ArrayList()

    private var handler: Handler?=null
    private var mMap: GoogleMap? = null
    private var marker : Marker?=null
    private var latitute: Double =0.0
    private var longitute: Double =0.0


    private var curLatitude: Double =0.0
    private var curLongitude: Double =0.0

    private val viewModel : ChooseAddressViewModel by viewModels()


    override fun getActivityBinding(): ActivityChooseAddressBinding = ActivityChooseAddressBinding.inflate(layoutInflater)

    override fun inits() {
        searchView()
        binding.clShadowButton.tvButtonLabel.text=getString(R.string.choose_this_location)
        startLocation(this, onResult, onPermissionLaucher, this)
    }

    private fun searchView() {
        binding.rvPlaces.layoutManager= LinearLayoutManager(this)
        binding.rvPlaces.adapter= AutoCompleteAddressAdapter(this, list, this)

        val v: View? = binding.svLocation.findViewById<View>(androidx.appcompat.R.id.search_plate)
        val textView: TextView? = binding.svLocation.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)
        textView?.typeface =ResourcesCompat.getFont(this, R.font.inter_regular)
        textView?.textSize=12f
        v?.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        binding.svLocation.queryHint = getString(R.string.search_city)
        binding.svLocation.isIconified=true


    }

    override fun initCtrl() {
        binding.svLocation.setOnClickListener(this)
        binding.svLocation.queryChanged {
            binding.svLocation.isIconified=false
            binding.rvPlaces.visibility=View.INVISIBLE
            binding.pbPlaces.visibility=View.VISIBLE
            (binding.rvPlaces.adapter as AutoCompleteAddressAdapter).filter.filter(it)
        }
        binding.svLocation.setOnCloseListener(this)
        binding.clShadowButton.ivButton.setOnClickListener(this)
        binding.btnCurrentLocation.setOnClickListener(this)
    }

    override fun observer() {
        lifecycleScope.launch {
            viewModel.addressResposne.observe(this@ChooseAddressActivity,{
                when(it){
                    is Resource.Success -> {
                        if(!it.value.isNullOrEmpty()){
                            if (it.value?.get(0)?.getAddressLine(0) != null) {
                                binding.clShadowButton.ivButton.enable(true)
                                binding.tvAddress.text=it.value?.get(0)?.getAddressLine(0) ?: ""
                            }
                            else binding.tvAddress.text="Sorry we didn't serve here yet..."
                        }else binding.tvAddress.text="Sorry we didn't serve here yet..."

                        binding.tvAddress.visible(true)
                        binding.pbAddressLoader.visible(false)
                        binding.clShadowButton.ivButton.visible(true)
                    }
                    is Resource.Failure ->{
                        binding.tvAddress.text="Sorry we didn't serve here yet..."
                        binding.tvAddress.visible(true)
                        binding.pbAddressLoader.visible(false)
                        binding.clShadowButton.ivButton.visible(true)
                    }
                }
            })
            viewModel.placeDetail.observe(this@ChooseAddressActivity,{
                 when(it){
                    is Resource.Success -> {
                        binding.pbLoader.visibility=View.GONE
                        latitute = it.value?.result?.geometry?.location?.lat ?: 0.0
                        longitute = it.value?.result?.geometry?.location?.lng ?: 0.0
                        if(marker!=null){
                            marker?.position = LatLng(it.value?.result?.geometry?.location?.lat?:0.0, it.value?.result?.geometry?.location?.lng?:0.0)
                        }else{
                            mMap?.apply {
                                marker=addMarker(MarkerOptions().position(LatLng(latitute,longitute)).title("Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.path_2740)))
                            }
                        }
                        mMap?.moveCamera(CameraUpdateFactory.newLatLng(marker?.position))
                        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(marker?.position, 21.0f))

                        binding.rvPlaces.visibility=View.GONE
                        binding.pbPlaces.visibility=View.GONE

                    }
                    is Resource.Failure ->{
                        binding.pbLoader.visibility=View.GONE
                        binding.root.snack(it.throwable?.message?:""){}
                    }
            }
        })
    }
    }


    private var onPermissionLaucher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions->
        var permission=true
        permissions.entries.forEach {
            if(!it.value) { permission=it.value }
        }
        when (permission){
            true -> { startLocation(this, onResultLaucher = onResult, null, this) }
            else ->{ binding.root.snack("Please Turn on GPS",Snackbar.LENGTH_LONG){
                action("Retry"){ startLocation(this@ChooseAddressActivity, onResult, onPermissionLaucher = snackViewPermissionLaucher, this@ChooseAddressActivity) }
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
            true -> { startLocation(this, onResultLaucher = onResult, null, this) }
            else ->{ binding.root.snack("Please Turn on GPS",Snackbar.LENGTH_LONG){ toast("Please again later")}
            }
        }
        }



    private var onResult = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
        if(it.resultCode== RESULT_OK){
            binding.pbLoader.visibility=View.VISIBLE
            loadCurrentLoc()
        }else{
            binding.root.snack("Please turn on GPS First"){}
        }
    }

    override fun onLocation(location: Location?) {
        binding.pbLoader.visibility=View.GONE
        latitute=location?.latitude?:0.0
        longitute=location?.longitude?:0.0
        curLongitude=longitute
        curLatitude=latitute
        val addresses: List<Address>
        val geocoder = Geocoder(this, Locale.getDefault())
        addresses = geocoder.getFromLocation(location?.latitude!!, location?.longitude!!, 1)
        binding.tvAddress.text = addresses[0]?.getAddressLine(0) ?: ""
        (supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?)?.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        mapControls()
        val currentLocation=LatLng(latitute,longitute)
        mMap?.apply {
            marker=addMarker(MarkerOptions().position(currentLocation).title("Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.path_2740)))
            mMap?.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 21.0f))
        }

        mMap?.setInfoWindowAdapter(AddressInfoWindow(this@ChooseAddressActivity))

        handler = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    GETTING_ADDRESS -> {
                        marker?.title = "Getting Address..!"
                        marker?.showInfoWindow()
                    }
                    NOT_SERVE_THIS_AREA -> {
                        marker?.title = "Sorry we didn't serve here yet..."
                        marker?.showInfoWindow()
                    }
                    HIDE_INFO_WINDOW -> { marker?.hideInfoWindow() }
                    else ->{
                        binding.pbLoader.visibility=View.VISIBLE
                    }
                }
            }
        }

        mMap?.setOnCameraMoveListener(this)
        mMap?.setOnCameraIdleListener(this)

        binding.pbAddressLoader.visible(true)
        binding.tvAddress.text=""
        binding.tvAddress.visible(false)
        binding.clShadowButton.ivButton.visible(false)
        binding.clShadowButton.ivButton.enable(false)

        viewModel.getCurrentAddres(context = this,
                                   latitute=currentLocation.latitude,
                                   longitute=currentLocation.longitude,
                                   handler=handler!!)
    }

    private fun mapControls() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
           // mMap?.isMyLocationEnabled = true
            mMap?.uiSettings?.isMyLocationButtonEnabled = true

        }
        mMap?.uiSettings?.isScrollGesturesEnabledDuringRotateOrZoom=false
        mMap?.uiSettings?.isZoomControlsEnabled=true

        // Location Button Gravity
        val btnMyLocation = (supportFragmentManager.findFragmentById(R.id.mapView)?.view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<View>(Integer.parseInt("2"))
        val params = RelativeLayout.LayoutParams( btnMyLocation.layoutParams.width, btnMyLocation.layoutParams.height) // size of button in dp
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
        params.setMargins(0, 0, 25, 0)
        btnMyLocation.layoutParams=params

        // Zoom Button
        mMap?.setPadding(0, 0, 0, 370)
    }


    override fun onCameraMove() {
        marker?.position = mMap?.cameraPosition?.target
        handler?.sendEmptyMessage(HIDE_INFO_WINDOW)
    }
    override fun onCameraIdle() {
        handler?.sendEmptyMessage(GETTING_ADDRESS)
        binding.pbAddressLoader.visible(true)
        binding.tvAddress.text=""
        binding.tvAddress.visible(false)
        binding.clShadowButton.ivButton.visible(false)
        binding.clShadowButton.ivButton.enable(false)
        viewModel.getCurrentAddres(context = this,
                                   latitute=mMap?.cameraPosition?.target?.latitude ?: 0.0,
                                   longitute=mMap?.cameraPosition?.target?.longitude ?: 0.0,
                                   handler=handler!!)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnCurrentLocation ->{ startActivity(Intent(this,MainActivity::class.java)) }
            R.id.ivButton->{ startActivity(Intent(this,MainActivity::class.java)) }
            else ->{ binding.svLocation.isIconified=false }
        }
    }

    override fun onClick(placeId: String?, spotName: String?) {
        binding.svLocation.isIconified=true

        binding.rvPlaces.visibility=View.GONE
        binding.pbPlaces.visibility=View.GONE
        binding.pbLoader.visibility=View.VISIBLE
        binding.root.hideKeyboard(this)
        viewModel.getLatLongFromPlaceId(placeId)
    }

    override fun loader(visible: Boolean?) {
        if(visible==true)
        {
          binding.rvPlaces.visibility=View.INVISIBLE
          binding.pbPlaces.visibility=View.VISIBLE
        }else{
          binding.rvPlaces.visibility=View.VISIBLE
          binding.pbPlaces.visibility=View.GONE
        }
    }

    override fun onClose(): Boolean {
        binding.rvPlaces.visibility=View.GONE
        binding.pbPlaces.visibility=View.GONE
        return false
    }

}