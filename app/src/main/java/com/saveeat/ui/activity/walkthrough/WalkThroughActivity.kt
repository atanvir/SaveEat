package com.saveeat.ui.activity.walkthrough

import android.content.Intent
import android.view.View
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityWalkThroughBinding
import com.saveeat.model.request.walkthrough.WalkThroughModel
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
                if(binding?.viewPager?.currentItem==2) startActivity(Intent(this, LoginWithPasswordActivity::class.java))
                else binding.viewPager.setCurrentItem(binding?.viewPager?.currentItem+1,true)
            }
            R.id.tvSkip -> { startActivity(Intent(this, LoginWithPasswordActivity::class.java)) }

        }
    }

    private fun walkthroughData(): MutableList<WalkThroughModel?>? {
        val list:MutableList<WalkThroughModel?>? = ArrayList()
        for(i in 0..2){
        val model = WalkThroughModel(image = 1,
                                     title = "Lorem ipsum dolor sit",
                                     description = "Lorem ipsum dolor sit amet, consectetur \n adipiscing elit")
        if(i==0) model?.image=R.drawable.group_740
        else if(i==1) model?.image=R.drawable.group_712
        else if(i==2)  model?.image=R.drawable.group_776
        list?.add(model)
        }
        return list
    }

}