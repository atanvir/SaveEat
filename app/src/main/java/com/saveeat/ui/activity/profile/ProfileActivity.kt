package com.saveeat.ui.activity.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.saveeat.R
import com.saveeat.aws.AWSListner
import com.saveeat.aws.AWSUtils
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityProfileBinding
import com.saveeat.model.request.profile.ProfileModel
import com.saveeat.repository.cache.PreferenceKeyConstants.countryCode
import com.saveeat.repository.cache.PreferenceKeyConstants.email
import com.saveeat.repository.cache.PreferenceKeyConstants.jwtToken
import com.saveeat.repository.cache.PreferenceKeyConstants.mobileNumber
import com.saveeat.repository.cache.PreferenceKeyConstants.name
import com.saveeat.repository.cache.PreferenceKeyConstants.profilePic
import com.saveeat.repository.cache.PrefrencesHelper.getPrefrenceStringValue
import com.saveeat.repository.cache.PrefrencesHelper.updateUserData
import com.saveeat.ui.activity.auth.otp.OTPVerificationActivity
import com.saveeat.ui.activity.main.MainActivity
import com.saveeat.utils.application.CommonUtils.buttonLoader
import com.saveeat.utils.application.CommonUtils.mobileNo
import com.saveeat.utils.application.CommonUtils.toolbar
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.FilePath.getPath
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.Resource
import com.saveeat.utils.extn.*
import com.saveeat.utils.permissions.camera.CameraHelper.cameraIntent
import com.saveeat.utils.permissions.camera.CameraHelper.checkCameraPermissions
import com.saveeat.utils.permissions.camera.CameraHelper.requestCameraPermission
import com.saveeat.utils.permissions.storage.StorageHelper.checkStoragePermissions
import com.saveeat.utils.permissions.storage.StorageHelper.requestStoragePermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import me.ibrahimsn.lib.PhoneNumberKit

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding>(), View.OnClickListener, AWSListner {
    private var path: String?=null
    private var phoneNumberKit : PhoneNumberKit? = null
    private val viewModel : ProfileViewModel by viewModels()
    private var mobileNo: String?=null


    override fun getActivityBinding(): ActivityProfileBinding = ActivityProfileBinding.inflate(layoutInflater)
    override fun inits() {
        mobileNo=getPrefrenceStringValue(this, countryCode)+getPrefrenceStringValue(this, mobileNumber)
        binding?.model= ProfileModel(profilePic =  getPrefrenceStringValue(this,profilePic),name = getPrefrenceStringValue(this,name),email =getPrefrenceStringValue(this,email),mobileNumber = getPrefrenceStringValue(this, mobileNumber),countryCode = getPrefrenceStringValue(this, countryCode),token = getPrefrenceStringValue(this, jwtToken))
        toolbar(this)
        phoneNumberKit = PhoneNumberKit(this)
        phoneNumberKit?.attachToInput(binding.tilPhoneNo, binding.countryCodePicker.selectedCountryCodeAsInt)
        phoneNumberKit?.setupCountryPicker(this, searchEnabled = true)

        isEditable(false)
        binding.clShadowButton.tvButtonLabel.text=getString(R.string.save_changes)
    }


    override fun initCtrl() {
        binding.tvEditProfile.setOnClickListener(this)
        binding.ivCamera.setOnClickListener(this)
        binding.clShadowButton.ivButton.setOnClickListener(this)
        binding.tieFullName.addTextChangedListener {
            binding.tvLabelFullName.text=it.toString()
            binding.tilFullName.isErrorEnabled=false
        }
        binding.tieEmail.addTextChangedListener { binding.tilEmail.isErrorEnabled=false }
        binding.tiePhoneNo.addTextChangedListener { binding.tilPhoneNo.isErrorEnabled=false }
    }
    override fun observer() {
        lifecycleScope.launch {
            viewModel.changeMobileNumber.observe(this@ProfileActivity, {
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) viewModel.userUpdateDetails(binding?.model)
                        else if(KeyConstants.FAILURE<=it.value?.status?:0){
                            buttonLoader(binding.clShadowButton,false)
                            ErrorUtil.snackView(binding.root, it.value?.message ?: "")
                        }
                    }
                    is Resource.Failure -> {
                        buttonLoader(binding.clShadowButton,false)
                        ErrorUtil.handlerGeneralError(binding.root, it.throwable!!)
                    }
                }
            })

            viewModel.userUpdateDetails.observe(this@ProfileActivity, {
                buttonLoader(binding.clShadowButton,false)
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            updateUserData(this@ProfileActivity,it.value?.data)
                            startActivity(Intent(this@ProfileActivity,MainActivity::class.java))
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) ErrorUtil.snackView(binding.root, it.value?.message ?: "")
                    }
                    is Resource.Failure -> { ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })
        }
    }

    private fun isEditable(isEditable: Boolean) {
        binding.ivCamera.visible(isEditable)
        binding.clShadowButton.clMainShadow.visible(isEditable)
        binding.clShadowButton.ivButton.enable(isEditable)

        binding.tieFullName.isFocusable=isEditable
        binding.tieFullName.isFocusableInTouchMode=isEditable
        binding.tilFullName.isFocusable=isEditable
        binding.tilFullName.isFocusableInTouchMode=isEditable

        binding.tieEmail.isFocusable=isEditable
        binding.tieEmail.isFocusableInTouchMode=isEditable
        binding.tieEmail.isFocusable=isEditable
        binding.tieEmail.isFocusableInTouchMode=isEditable

        binding.tiePhoneNo.isFocusable=isEditable
        binding.tiePhoneNo.isFocusableInTouchMode=isEditable
        binding.tiePhoneNo.isFocusable=isEditable
        binding.tiePhoneNo.isFocusableInTouchMode=isEditable

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivButton ->{
                buttonLoader(binding.clShadowButton,true)
                if(checkValidation()) {
                binding?.model?.countryCode= mobileNo(binding?.tiePhoneNo)?.first
                binding?.model?.mobileNumber= mobileNo(binding?.tiePhoneNo)?.second
                if(!isMobileChange()) {
                    buttonLoader(binding.clShadowButton,false)
                    laucher.launch(Intent(this,OTPVerificationActivity::class.java).putExtra("data",binding?.model))
                }
                else viewModel.userUpdateDetails(binding?.model)
            }else buttonLoader(binding.clShadowButton,false)
            }
            R.id.ivCamera ->{
                if (!checkCameraPermissions(this)) requestCameraPermission(onCameraLaucher)
                else if (!checkStoragePermissions(this)) requestStoragePermission(this, onScopeResultLaucher = onScopeResultLaucher, onPermissionlaucher = onPermissionlaucher)
                else path=cameraIntent(this,resultLauncher)
            }

            R.id.tvEditProfile -> {
                binding.tvEditProfile.visible(false)
                isEditable(true)
            }
        }
    }

    private var laucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ it->
        if(it.resultCode== RESULT_OK){
            buttonLoader(binding.clShadowButton,true)
            viewModel.changeMobileNumber(binding?.model)
        }
    }

    private fun isMobileChange(): Boolean {
        return (mobileNo==(binding?.model?.countryCode+""+binding?.model?.mobileNumber).replace(" ",""))
    }

    private fun checkValidation(): Boolean {
        var ret=true
        if(binding?.model?.name.isNullOrEmpty()) {
            ret=false
            binding.tilFullName.isErrorEnabled=true
            binding.tilFullName.error=getString(R.string.please_enter_full_name)

        }else if(binding?.model?.email.isNullOrEmpty()){
            ret=false
            binding.tilEmail.isErrorEnabled=true
            binding.tilEmail.error=getString(R.string.please_enter_emailId)
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding?.model?.email?:"").matches()){
            ret=false
            binding.tilEmail.isErrorEnabled=true
            binding.tilEmail.error=getString(R.string.valid_email_id)
        }else if(mobileNo(binding.tiePhoneNo)?.second.isNullOrEmpty()){
            ret=false
            binding.tilPhoneNo.isErrorEnabled=true
            binding.tilPhoneNo.error=getString(R.string.please_enter_phone_number)

        }else if(phoneNumberKit?.isValid==false){
            ret=false
            binding.tilPhoneNo.isErrorEnabled=true
            binding.tilPhoneNo.error=getString(R.string.please_enter_valid_phone_number)
        }
        return ret

    }


    private var onScopeResultLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            path=cameraIntent(this,resultLauncher)
        }
    }

    private var onCameraLaucher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions->
        var permission=true
        permissions.entries.forEach {
            if(!it.value){ permission=it.value }
        }
        when (permission){
            true -> {
                if (!checkStoragePermissions(this)) requestStoragePermission(this, onScopeResultLaucher = onScopeResultLaucher, onPermissionlaucher = onPermissionlaucher)
                else path=cameraIntent(this,resultLauncher)
            }
            else ->{ binding.root.snack(getString(R.string.please_allow_permission_to_access_camera)){} }
        }
    }

    private var onPermissionlaucher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        var permission=true
        permissions.entries.forEach {
            if(!it.value){ permission=it.value }
        }
        when(permission){
            true ->{ path=cameraIntent(this,resultLauncher) }
            else ->{ binding.root.snack(getString(R.string.please_allow_permission_to_access_camera)){} }
        }
    }


    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            if(result.data!=null){
                path = getPath(this, Uri.parse(result?.data?.dataString))
            }
            AWSUtils(this,path,this)
        }
    }

    override fun onAWSLoader(isLoader: Boolean) {
        binding.progressbar.visibility=View.VISIBLE
    }

    override fun onAWSSuccess(url: String?) {
        binding.progressbar.visibility=View.GONE
        binding?.model?.profilePic=url
        binding.ivProfile.loadProfilePic(url,binding.progressbar)
    }

    override fun onAWSError(error: String?) {
        binding.progressbar.visibility=View.GONE
        binding.root.snack(error?:""){}
    }

    override fun onAWSProgress(progress: Int?) {
        Log.e("progress", "uploading done $progress")
    }

}