package com.saveeat.ui.activity.drawer.credit

import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import androidx.fragment.app.DialogFragment
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityCreditBinding
import com.saveeat.ui.dialog.credit.CreditDialog
import com.saveeat.utils.application.CommonUtils.toolbar
import dagger.hilt.android.AndroidEntryPoint
import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.saveeat.BuildConfig
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.repository.cache.PrefrencesHelper
import com.saveeat.ui.activity.auth.otp.OTPVerificationActivity
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.Resource
import kotlinx.coroutines.launch
import kotlin.math.roundToLong


@AndroidEntryPoint
class CreditActivity : BaseActivity<ActivityCreditBinding>(), View.OnClickListener {
    private val viewModel : CreditViewModel by viewModels()
    override fun getActivityBinding(): ActivityCreditBinding = ActivityCreditBinding.inflate(layoutInflater)

    override fun inits() {
        toolbar(this)
        viewModel.getUserDetail(token=PrefrencesHelper.getPrefrenceStringValue(this,PreferenceKeyConstants.jwtToken))
        val wordtoSpan: Spannable = SpannableString(binding.tvLabel.text.toString())
        wordtoSpan.setSpan(StyleSpan(Typeface.BOLD), 0, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvLabel.text=wordtoSpan
        binding.clShadowButton.tvButtonLabel.text=getString(R.string.invite_friends)
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
              binding.ivInvite.setImageResource(R.drawable.group_invite)
            binding.clShadowButton.clMainShadow.visibility= View.VISIBLE
        },1500)

    }

    override fun initCtrl() {
        binding.btnAddCredit.setOnClickListener(this)
        binding.clShadowButton.ivButton.setOnClickListener(this)
    }

    override fun observer() {
        lifecycleScope.launch {
            viewModel.getUserDetails.observe(this@CreditActivity,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            binding.tvAmount.text=this@CreditActivity?.getString(R.string.price,it.value?.data?.walletAmount?:0.0.toString())
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) ErrorUtil.snackView(binding.root, it.value?.message ?: "")
                    }
                    is Resource.Failure -> {
                        ErrorUtil.handlerGeneralError(binding.root, it.throwable!!)
                    }
                }
            })
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAddCredit ->{
                val dialog = CreditDialog()
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_NoTitle);
                dialog.show(supportFragmentManager, "")
            }

            R.id.ivButton ->{
                val link = "https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID
                val uri: Uri = Uri.parse(link)
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, link)
                intent.putExtra(Intent.EXTRA_TITLE, "Credit")
                startActivity(Intent.createChooser(intent, "Share Link"))
            }
        }
    }
}