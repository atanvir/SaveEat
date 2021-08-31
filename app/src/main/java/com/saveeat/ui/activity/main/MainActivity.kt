package com.saveeat.ui.activity.main

import android.content.Intent
import android.location.Address
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityMainBinding
import com.saveeat.model.response.saveeat.location.LocationModel
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.repository.cache.PreferenceKeyConstants.address
import com.saveeat.repository.cache.PreferenceKeyConstants.distance
import com.saveeat.repository.cache.PreferenceKeyConstants.latitude
import com.saveeat.repository.cache.PreferenceKeyConstants.longitude
import com.saveeat.repository.cache.PreferenceKeyConstants.profilePic
import com.saveeat.repository.cache.PrefrencesHelper.getPrefrenceStringValue
import com.saveeat.repository.cache.PrefrencesHelper.updateLocation
import com.saveeat.ui.activity.drawer.drawer.DrawerActivity
import com.saveeat.ui.activity.location.ChooseAddressActivity
import com.saveeat.ui.activity.profile.ProfileActivity
import com.saveeat.ui.bottomsheet.distance.DistanceBottomSheet
import com.saveeat.utils.application.CommonUtils.setSpinner
import com.saveeat.utils.extn.loadProfilePic
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import android.location.Geocoder




@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), View.OnClickListener {

    private lateinit var navController : NavController

    override fun getActivityBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun inits() {
        navController=findNavController(this,R.id.fragment)
        binding.bottomNavigationView.setupWithNavController(navController)
        if(intent.getBooleanExtra("menu",false)) binding.bottomNavigationView.selectedItemId=R.id.cartFragment
    }

    override fun onResume() {
        super.onResume()
        refreshToolbar()
    }


    override fun initCtrl() {
        binding.clMainToolbar.apply {
            ivProfile.setOnClickListener(this@MainActivity)
            ivDrawer.setOnClickListener(this@MainActivity)
            tvAddress.setOnClickListener(this@MainActivity)
            tvKMDropDown.setOnClickListener(this@MainActivity)
        }
    }

    override fun observer() {


    }


    override fun onClick(v: View?) {
        when(v?.id){

            // Toolbar
            R.id.ivProfile ->{ startActivity(Intent(this,ProfileActivity::class.java)) }
            R.id.tvKMDropDown ->{ DistanceBottomSheet({
                binding.clMainToolbar.tvKMDropDown.text="Within $it KM"
                binding.clMainToolbar.tvKMDropDown.tag=it?.toString() },
                (binding.clMainToolbar.tvKMDropDown.tag?:"").toString()).show(supportFragmentManager,"") }

            // Drawer
            R.id.ivDrawer ->{ startActivity(Intent(this, DrawerActivity::class.java))}
            R.id.tvAddress -> { laucher.launch(Intent(this,ChooseAddressActivity::class.java).putExtra("data", LocationModel(address = getPrefrenceStringValue(this,address),latitude=getPrefrenceStringValue(this,latitude).toDouble(),longitude=getPrefrenceStringValue(this, longitude).toDouble(),distance=getPrefrenceStringValue(this,distance)))) }

        }
    }


    private var laucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ it->
        if(it.resultCode== RESULT_OK){
         val location=it.data?.getParcelableExtra<LocationModel>("data")
         updateLocation(this,location)
         refreshToolbar()
        }
    }




    private fun refreshToolbar() {
        getLocalityFromLatLng()
        binding.clMainToolbar.ivProfile.loadProfilePic(getPrefrenceStringValue(this, profilePic),binding.clMainToolbar.progresBar)
        Handler(Looper.getMainLooper()!!).postDelayed(Runnable {
            binding.clMainToolbar.tvKMDropDown.text= "Within "+getPrefrenceStringValue(this, distance) +" KM"
            binding.clMainToolbar.tvKMDropDown.tag=getPrefrenceStringValue(this, distance)
        },500)
    }

    private fun getLocalityFromLatLng() {
        val addresses : List<Address?>? = Geocoder(this, Locale.getDefault()).getFromLocation(getPrefrenceStringValue(this,latitude).toDouble(), getPrefrenceStringValue(this, longitude).toDouble(), 1)
        binding.clMainToolbar.tvAddress.text=addresses?.get(0)?.subLocality
    }


}