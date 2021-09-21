package com.saveeat.ui.activity.drawer.drawer

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityDrawerBinding
import com.saveeat.model.request.badge.BadgeModel
import com.saveeat.repository.cache.PreferenceKeyConstants._id
import com.saveeat.repository.cache.PreferenceKeyConstants.jwtToken
import com.saveeat.repository.cache.PrefrencesHelper
import com.saveeat.repository.cache.PrefrencesHelper.getPrefrenceStringValue
import com.saveeat.ui.activity.auth.login.otp.LoginWithOTPActivity
import com.saveeat.ui.activity.auth.login.password.LoginWithPasswordActivity
import com.saveeat.ui.activity.drawer.help.HelpActivity
import com.saveeat.ui.activity.drawer.credit.CreditActivity
import com.saveeat.ui.activity.drawer.history.OrderHistoryActivity
import com.saveeat.ui.activity.drawer.location.HiddenLocationActivity
import com.saveeat.ui.activity.drawer.payment.PaymentActivity
import com.saveeat.ui.activity.drawer.reward.RewardActivity
import com.saveeat.ui.adapter.rewards.RewardsAdapter
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DrawerActivity : BaseActivity<ActivityDrawerBinding>(), View.OnClickListener {
    private val viewModel : DrawerViewModel by viewModels()

    override fun getActivityBinding(): ActivityDrawerBinding = ActivityDrawerBinding.inflate(layoutInflater)
    override fun inits() {
        binding.progresBar.visibility=View.VISIBLE
        viewModel.userBadgesEarning(BadgeModel(token = getPrefrenceStringValue(this, jwtToken),
                                               userId = getPrefrenceStringValue(this,_id)))
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
        lifecycleScope.launch {
            viewModel.userLogout.observe(this@DrawerActivity,{
                binding.tvLogout.isEnabled=true
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            PrefrencesHelper.userLogout(this@DrawerActivity)
                            startActivity(Intent(this@DrawerActivity, LoginWithOTPActivity::class.java))
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) ErrorUtil.snackView(binding.root, it.value?.message ?: "")
                    }
                    is Resource.Failure -> { ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })


            viewModel.userBadgesEarning.observe(this@DrawerActivity,{
                binding.progresBar.visibility=View.GONE
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            binding.model=it.value?.data
                            binding.rvRewards.layoutManager = LinearLayoutManager(this@DrawerActivity, LinearLayoutManager.HORIZONTAL,false)
                            binding.rvRewards.adapter = RewardsAdapter(this@DrawerActivity, it.value?.data?.badgeData)
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) ErrorUtil.snackView(binding.root, it.value?.message ?: "")
                    }
                    is Resource.Failure -> { ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })

        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.ivBack ->{ onBackPressed() }
            R.id.ivRewards ->{ startActivity(Intent(this, RewardActivity::class.java)) }
            R.id.tvPaymentInformation ->{ startActivity(Intent(this, PaymentActivity::class.java)) }
            R.id.tvOrderDetail ->{ startActivity(Intent(this,OrderHistoryActivity::class.java)) }
            R.id.tvCreditWallet ->{ startActivity(Intent(this, CreditActivity::class.java)) }
            R.id.tvHiddenLocation ->{ startActivity(Intent(this,HiddenLocationActivity::class.java)) }
            R.id.tvHelp ->{ startActivity(Intent(this,HelpActivity::class.java)) }
            R.id.tvLogout ->{
                binding.tvLogout.isEnabled=false
                viewModel.userLogout(getPrefrenceStringValue(this, jwtToken))
            }
        }
    }
}