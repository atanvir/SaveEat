package com.saveeat.ui.activity.drawer.location

import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityHiddenLocationBinding
import com.saveeat.utils.application.CommonUtils.toolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HiddenLocationActivity : BaseActivity<ActivityHiddenLocationBinding>() {

    override fun getActivityBinding(): ActivityHiddenLocationBinding = ActivityHiddenLocationBinding.inflate(layoutInflater)

    override fun inits() {
        toolbar(this)
        binding.clShadowButton.tvButtonLabel.text=getString(R.string.unlock_location)
    }

    override fun initCtrl() {
    }

    override fun observer() {
    }
}