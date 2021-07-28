package com.saveeat.ui.activity.drawer.reward

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityRewardBinding
import com.saveeat.ui.adapter.content.FAQAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RewardActivity : BaseActivity<ActivityRewardBinding>(), View.OnClickListener {
    override fun getActivityBinding(): ActivityRewardBinding = ActivityRewardBinding.inflate(layoutInflater)

    override fun inits() {
        binding.rvFaq.layoutManager=LinearLayoutManager(this)
        binding.rvFaq.adapter=FAQAdapter(this)
    }

    override fun initCtrl() {
        binding.ivClose.setOnClickListener(this)
    }

    override fun observer() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivClose ->{ onBackPressed() }
        }
    }
}