package com.saveeat.ui.activity.main

import android.content.Intent
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityMainBinding
import com.saveeat.ui.activity.drawer.DrawerActivity
import com.saveeat.ui.activity.location.ChooseAddressActivity
import com.saveeat.ui.activity.profile.ProfileActivity
import com.saveeat.utils.application.CommonUtils.setSpinner
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), View.OnClickListener {

    private lateinit var navController : NavController

    override fun getActivityBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun inits() {
        navController=findNavController(this,R.id.fragment)
        binding.bottomNavigationView.setupWithNavController(navController)
        if(intent.getBooleanExtra("menu",false)) binding.bottomNavigationView.selectedItemId=R.id.cartFragment
        setSpinner(this,binding.clMainToolbar.spnAddress,binding.clMainToolbar.tvKMDropDown)
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
            R.id.tvKMDropDown ->{ binding.clMainToolbar.spnAddress.performClick() }

            // Drawer
            R.id.ivDrawer ->{ startActivity(Intent(this,DrawerActivity::class.java))}
            R.id.tvAddress -> {startActivity(Intent(this,ChooseAddressActivity::class.java))}

        }
    }


}