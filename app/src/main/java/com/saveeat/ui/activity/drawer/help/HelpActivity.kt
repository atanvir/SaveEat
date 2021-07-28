package com.saveeat.ui.activity.drawer.help

import androidx.recyclerview.widget.LinearLayoutManager
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityHelpBinding
import com.saveeat.ui.adapter.content.FAQAdapter
import com.saveeat.utils.application.CommonUtils.toolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpActivity : BaseActivity<ActivityHelpBinding>() {
    override fun getActivityBinding(): ActivityHelpBinding = ActivityHelpBinding.inflate(layoutInflater)

    override fun inits() {
        toolbar(this)
        binding.rvFAQ.layoutManager=LinearLayoutManager(this)
        binding.rvFAQ.adapter= FAQAdapter(this)
    }

    override fun initCtrl() {
    }

    override fun observer() {
    }

}