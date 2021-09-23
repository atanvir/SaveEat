package com.saveeat.ui.activity.walkthrough

import android.content.Intent
import android.view.View
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityWalkThroughBinding
import com.saveeat.model.request.walkthrough.WalkThroughModel
import com.saveeat.ui.activity.auth.login.otp.LoginWithOTPActivity
import com.saveeat.ui.activity.auth.login.password.LoginWithPasswordActivity
import com.saveeat.ui.adapter.walkthrough.WalkThroughAdapter
import com.saveeat.utils.extn.onPageChanged
import com.saveeat.utils.extn.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalkThroughActivity : BaseActivity<ActivityWalkThroughBinding>(), View.OnClickListener {

    override fun getActivityBinding(): ActivityWalkThroughBinding = ActivityWalkThroughBinding.inflate(layoutInflater)

    override fun inits() {
        binding.viewPager.adapter= WalkThroughAdapter(this, walkthroughData())
        binding.tabLayout.setupWithViewPager(binding.viewPager, true)
    }

    override fun initCtrl() {
        binding.ivNext.setOnClickListener(this)
        binding.tvSkip.setOnClickListener(this)
        binding.viewPager.onPageChanged{ position,positionOffset,positionOffsetPixels ->
            if(position==2) binding.tvSkip.visible(false)
            else binding.tvSkip.visible(true)
        }
    }

    override fun observer() {
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivNext -> {
                if(binding?.viewPager?.currentItem==2) startActivity(Intent(this, LoginWithOTPActivity::class.java))
                else binding.viewPager.setCurrentItem(binding?.viewPager?.currentItem+1,true)
            }
            R.id.tvSkip -> { startActivity(Intent(this, LoginWithOTPActivity::class.java)) }

        }
    }

    private fun walkthroughData(): MutableList<WalkThroughModel?>? {
        val list:MutableList<WalkThroughModel?>? = ArrayList()
        for(i in 0..2){
            var model: WalkThroughModel?=null
            if(i ==0){
              model = WalkThroughModel(image = R.drawable.burger_transparent,
                                     title = "Welcome to SaveEat!",
                                     description = "All you need to save the world: EAT")
        }else if(i==1){
             model = WalkThroughModel(image = R.drawable.takeaway_transparent,
                title = "Pay in the app & pick up",
                description = "Pay directly in the app and pick up the food as take away by giving your order OTP")
        }
        else if(i==2){
             model = WalkThroughModel(image = R.drawable.notification_transparent,
                title = "Never miss a deal",
                description = "Follow your favourite restaurants in the app, and get notifications when they have food available on a discount.\n")
        }
        list?.add(model)
        }
        return list
    }

}