package com.saveeat.ui.activity.auth.otp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityOtpverificationBinding
import com.saveeat.model.request.profile.ProfileModel
import com.saveeat.model.request.auth.forgot.ForgotModel
import com.saveeat.model.request.auth.signup.SignupModel
import com.saveeat.ui.activity.auth.password.PasswordActivity
import com.saveeat.utils.application.CommonUtils.TAG
import com.saveeat.utils.application.CommonUtils.authToolbar
import com.saveeat.utils.application.CommonUtils.buttonLoader
import com.saveeat.utils.application.ErrorUtil.snackView
import com.saveeat.utils.extn.hideKeyboard
import com.saveeat.utils.extn.text
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class OTPVerificationActivity : BaseActivity<ActivityOtpverificationBinding>(), View.OnClickListener, OnSuccessListener<Void>, OnFailureListener, OnCompleteListener<AuthResult> {
    private lateinit var mAuth: FirebaseAuth
    var data: Any?=null
    var mobileNo: String?=null
    var smsReceiver: BroadcastReceiver?=null
    private var verificationCode = ""
    private var mResendToken: PhoneAuthProvider.ForceResendingToken?=null
    private var countDownTimer : CountDownTimer?=null

    private val OTP_TIMER: Long=119000
    private val OTP_TIMER_DELAY: Long=1000
    override fun getActivityBinding(): ActivityOtpverificationBinding = ActivityOtpverificationBinding.inflate(layoutInflater)

    override fun inits() {
       authToolbar(this)
       mAuth = FirebaseAuth.getInstance()
       mAuth.setLanguageCode("en")
       binding.clShadowButton.tvButtonLabel.text=getString(R.string.submit)
       data=intent?.getParcelableExtra("data")
        when (data) {
            is SignupModel -> { mobileNo= (data as SignupModel)?.countryCode + (data as SignupModel)?.mobileNumber }
            is ForgotModel -> { mobileNo= (data as ForgotModel)?.countryCode + (data as ForgotModel)?.mobileNumber }
            is ProfileModel -> { mobileNo= (data as ProfileModel)?.countryCode + (data as ProfileModel)?.mobileNumber }
        }
       startTimer()
       autoReadOTP()
       sendOTP()
    }

    override fun initCtrl() {
        binding.tvResendOtp.setOnClickListener(this)
        binding.clShadowButton.ivButton.setOnClickListener(this)
    }

    override fun observer() {
        smsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {
                    when ((intent.extras?.get(SmsRetriever.EXTRA_STATUS) as Status).statusCode) {
                        CommonStatusCodes.SUCCESS -> { Log.e(TAG(this@OTPVerificationActivity),intent.extras?.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String) }
                        CommonStatusCodes.TIMEOUT -> { Log.e(TAG(this@OTPVerificationActivity), "TIMEOUT") }
                        else ->{ Log.e(TAG(this@OTPVerificationActivity),"status does not match")}
                    }
                }
            } }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tvResendOtp ->{ startTimer()
                                 sendOTP() }

            R.id.ivButton ->{

                /*---------------- With Verification ----------------*/
                v.hideKeyboard(this)
                if (verificationCode.equals("", ignoreCase = true)) snackView(binding.root, "Please wait for the OTP")
                else if (binding?.otpView?.text()?.isNotEmpty()) verifyVerificationCode(binding.otpView.text() ?: "")
                else snackView(binding.root, "Please enter OTP")


                /*---------------- Without Verification ----------------
                buttonLoader(binding.clShadowButton,false)
                    when (data) {
                        is SignupModel  -> { startActivity(Intent(this,PasswordActivity::class.java).putExtra("data",data as SignupModel)) }
                        is ForgotModel  -> { startActivity(Intent(this,PasswordActivity::class.java).putExtra("data",data as ForgotModel)) }
                        is ProfileModel -> { setResult(RESULT_OK,Intent().putExtra("data",data as ProfileModel))
                                             finish() }
                    }
                */
            }

        }
    }

    private fun autoReadOTP() {
        val client = SmsRetriever.getClient(this)
        val task = client.startSmsRetriever()
        task.addOnSuccessListener(this)
        task.addOnFailureListener(this)
    }

    private fun sendOTP(){
        lifecycleScope.launch {
            val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(mobileNo ?: "")
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this@OTPVerificationActivity)
                .setCallbacks(mCallback)
            if(mResendToken!=null) options.setForceResendingToken(mResendToken!!)
            PhoneAuthProvider.verifyPhoneNumber(options.build())
        }
    }


    private fun startTimer() {
        if(countDownTimer!=null) countDownTimer=null
        countDownTimer = object : CountDownTimer(OTP_TIMER, OTP_TIMER_DELAY) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimer.visibility=View.VISIBLE
                binding.tvResendOtp.visibility=View.INVISIBLE
                binding.tvTimer.text="" + String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))
            }

            override fun onFinish() {
                binding.tvTimer.visibility=View.INVISIBLE
                binding.tvResendOtp.visibility=View.VISIBLE
            }
        }.start()
    }



    private fun verifyVerificationCode(otp: String) {
        val credential = PhoneAuthProvider.getCredential(verificationCode, otp)
        signInWithPhoneAuthCredential(credential)
    }

    private var mCallback:PhoneAuthProvider.OnVerificationStateChangedCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            binding.root.hideKeyboard(this@OTPVerificationActivity)
            if(credential?.smsCode!=null) {
                binding?.otpView.setText("" + credential?.smsCode!!)
                signInWithPhoneAuthCredential(credential)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            buttonLoader(binding.clShadowButton,false)
            binding?.otpView.hideKeyboard(this@OTPVerificationActivity)
            snackView(binding.root,e.message?:"")
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            buttonLoader(binding.clShadowButton,false)
            verificationCode = verificationId
            mResendToken = token
        }
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        buttonLoader(binding.clShadowButton,true)
        mAuth?.signInWithCredential(credential)?.addOnCompleteListener(this)
    }
    override fun onComplete(task: Task<AuthResult>) {
        buttonLoader(binding.clShadowButton,false)
        if (task.isSuccessful) {
            when (data) {
                is SignupModel  -> { startActivity(Intent(this,PasswordActivity::class.java).putExtra("data",data as SignupModel)) }
                is ForgotModel  -> { startActivity(Intent(this,PasswordActivity::class.java).putExtra("data",data as ForgotModel)) }
                is ProfileModel -> { setResult(RESULT_OK,Intent().putExtra("data",data as ProfileModel))
                                     finish() }
            }
        } else {
            snackView(binding.root,task.exception?.message?:"")
        }
    }

    override fun onSuccess(p0: Void?) {
        val filter = IntentFilter()
        filter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        this.registerReceiver(smsReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        this.unregisterReceiver(smsReceiver)
    }

    override fun onFailure(p0: Exception) { Log.e(TAG(this),p0.message?:"") }
}