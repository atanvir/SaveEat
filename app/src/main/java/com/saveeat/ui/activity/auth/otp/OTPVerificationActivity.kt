package com.saveeat.ui.activity.auth.otp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityOtpverificationBinding
import com.saveeat.ui.activity.auth.password.PasswordActivity
import com.saveeat.utils.application.CommonUtils
import com.saveeat.utils.application.CommonUtils.authToolbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class OTPVerificationActivity : BaseActivity<ActivityOtpverificationBinding>(), View.OnClickListener {
    private val OTP_TIMER: Long=119000
    private val OTP_TIMER_DELAY: Long=1000
    override fun getActivityBinding(): ActivityOtpverificationBinding = ActivityOtpverificationBinding.inflate(layoutInflater)

    override fun inits() {
     binding.clShadowButton.tvButtonLabel.text=getString(R.string.submit)
     authToolbar(this)

        binding.tvResendOtp.visibility=View.GONE
       object : CountDownTimer(OTP_TIMER, OTP_TIMER_DELAY) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimer.text="" + String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))
            }

            override fun onFinish() {
                binding.tvResendOtp.visibility=View.VISIBLE
                binding.tvTimer.visibility=View.GONE
            }
        }.start()
    }

    override fun initCtrl() {
        binding.tvResendOtp.setOnClickListener(this)
        binding.clShadowButton.ivButton.setOnClickListener(this)
    }

    override fun observer() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tvResendOtp ->{
                binding.tvTimer.visibility=View.VISIBLE
                binding.tvResendOtp.visibility=View.GONE
            }

            R.id.ivButton ->{
                startActivity(Intent(this,PasswordActivity::class.java).putExtra("type",intent.getStringExtra("type")))
            }

        }
    }
}