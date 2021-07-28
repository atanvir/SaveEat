package com.saveeat.ui.activity.drawer

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityDrawerBinding
import com.saveeat.ui.activity.auth.login.LoginActivity
import com.saveeat.ui.activity.drawer.help.HelpActivity
import com.saveeat.ui.activity.drawer.credit.CreditActivity
import com.saveeat.ui.activity.order.history.OrderHistoryActivity
import com.saveeat.ui.activity.drawer.location.HiddenLocationActivity
import com.saveeat.ui.activity.drawer.payment.PaymentActivity
import com.saveeat.ui.activity.drawer.reward.RewardActivity
import com.saveeat.ui.adapter.rewards.RewardsAdapter
import com.saveeat.utils.application.StaticDataHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DrawerActivity : BaseActivity<ActivityDrawerBinding>(), View.OnClickListener {
    override fun getActivityBinding(): ActivityDrawerBinding = ActivityDrawerBinding.inflate(layoutInflater)
    override fun inits() {
        binding.rvRewards.apply {
           layoutManager= LinearLayoutManager(this@DrawerActivity, LinearLayoutManager.HORIZONTAL,false)
           adapter= RewardsAdapter(this@DrawerActivity, StaticDataHelper.getRewardModel())
        }
    }

    override fun initCtrl() {
        binding.tvPaymentInformation.setOnClickListener(this@DrawerActivity)
        binding.tvCreditWallet.setOnClickListener(this@DrawerActivity)
        binding.tvOrderDetail.setOnClickListener(this@DrawerActivity)
        binding.tvHiddenLocation.setOnClickListener(this@DrawerActivity)
        binding.tvHelp.setOnClickListener(this@DrawerActivity)
        binding.tvLogout.setOnClickListener(this@DrawerActivity)
        binding.ivRewards.setOnClickListener(this@DrawerActivity)
        binding.ivBack.setOnClickListener(this@DrawerActivity)
    }

    override fun observer() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            // Slider bar
            R.id.ivBack ->{onBackPressed()}
            R.id.ivRewards ->{ startActivity(Intent(this, RewardActivity::class.java))}
            R.id.tvPaymentInformation ->{ startActivity(Intent(this, PaymentActivity::class.java))}
            R.id.tvOrderDetail ->{ startActivity(Intent(this,OrderHistoryActivity::class.java))}
            R.id.tvCreditWallet ->{ startActivity(Intent(this, CreditActivity::class.java))}
            R.id.tvHiddenLocation ->{ startActivity(Intent(this,HiddenLocationActivity::class.java)) }
            R.id.tvHelp ->{ startActivity(Intent(this,HelpActivity::class.java))}
            R.id.tvLogout ->{startActivity(Intent(this, LoginActivity::class.java))}
        }
    }
}