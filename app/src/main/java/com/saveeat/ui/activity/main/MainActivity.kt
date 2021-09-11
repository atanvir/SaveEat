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
import com.saveeat.utils.extn.loadProfilePic
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import android.location.Geocoder
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.NavigationUI
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.repository.cache.PrefrencesHelper
import com.saveeat.ui.adapter.cart.CartAdapter
import com.saveeat.ui.fragment.main.cart.CartViewModel
import com.saveeat.utils.application.CustomLoader
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.Resource
import kotlinx.coroutines.launch

import com.google.android.material.badge.BadgeDrawable
import kotlin.math.roundToInt


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), View.OnClickListener {

    private lateinit var navController : NavController
    private val viewModel : MainViewModel by viewModels()


    override fun getActivityBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun inits() {
        navController=findNavController(this,R.id.fragment)
        binding.bottomNavigationView.setupWithNavController(navController)
        if(intent.getBooleanExtra("menu",false)) binding.bottomNavigationView.selectedItemId=R.id.cartFragment
    }

    override fun onResume() {
        super.onResume()
        refreshToolbar()
        viewModel.getCartCount(token = getPrefrenceStringValue(this, PreferenceKeyConstants.jwtToken))
    }



    override fun initCtrl() {
        binding.clMainToolbar.apply {
            ivProfile.setOnClickListener(this@MainActivity)
            ivDrawer.setOnClickListener(this@MainActivity)
            tvAddress.setOnClickListener(this@MainActivity)
            tvKMDropDown.setOnClickListener(this@MainActivity)
        }
        binding.bottomNavigationView.setOnNavigationItemSelectedListener  { item ->
            if(item.itemId != binding.bottomNavigationView.selectedItemId) NavigationUI.onNavDestinationSelected(item, navController)
            true
        }
    }

    override fun observer() {
        lifecycleScope.launch {
         viewModel.getCartCount.observe(this@MainActivity,{
             CustomLoader.hideLoader()
             when (it) {
                 is Resource.Success -> {
                     if(KeyConstants.SUCCESS==it.value?.status?:0) {
                        if(it.value?.data?:0.0>0.0){
                            binding.bottomNavigationView.removeBadge(R.id.cartFragment)
                            binding.bottomNavigationView.getOrCreateBadge(R.id.cartFragment).number = it.value?.data?.roundToInt()!!
                        }
                        else binding.bottomNavigationView.removeBadge(R.id.cartFragment)
                     }
                     else if(KeyConstants.FAILURE<=it.value?.status?:0) ErrorUtil.snackView(binding.root,it.value?.message?:"")
                 }
                 is Resource.Failure -> { CustomLoader.hideLoader(); ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
             }
         })
        }
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
        if(it.resultCode== RESULT_OK) {
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
        if(addresses?.get(0)?.subLocality?:"".equals("")==true) binding.clMainToolbar.tvAddress.text=addresses?.get(0)?.getAddressLine(0)
        else binding.clMainToolbar.tvAddress.text=addresses?.get(0)?.subLocality
    }
}