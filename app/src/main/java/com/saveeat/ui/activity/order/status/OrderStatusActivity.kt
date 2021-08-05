package com.saveeat.ui.activity.order.status

import android.content.Intent
import android.view.View
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityOrderStatusBinding
import com.saveeat.ui.activity.drawer.history.OrderHistoryActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderStatusActivity : BaseActivity<ActivityOrderStatusBinding>(), View.OnClickListener {
    override fun getActivityBinding(): ActivityOrderStatusBinding = ActivityOrderStatusBinding.inflate(layoutInflater)

    override fun inits() {
        binding.clShadowButton.tvButtonLabel.text="Check Status"
    }

    override fun initCtrl() {
        binding.clShadowButton.ivButton.setOnClickListener(this)
    }

    override fun observer() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivButton->{ startActivity(Intent(this, OrderHistoryActivity::class.java)) }
        }
    }

}