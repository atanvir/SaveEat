package com.saveeat.ui.activity.profile

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityProfileBinding
import com.saveeat.utils.application.CommonUtils.toolbar
import com.saveeat.utils.application.FilePath.getPath
import com.saveeat.utils.extn.*
import com.saveeat.utils.permissions.camera.CameraHelper.cameraIntent
import com.saveeat.utils.permissions.camera.CameraHelper.checkCameraPermissions
import com.saveeat.utils.permissions.camera.CameraHelper.requestCameraPermission
import com.saveeat.utils.permissions.storage.StorageHelper.checkStoragePermissions
import com.saveeat.utils.permissions.storage.StorageHelper.requestStoragePermission
import me.ibrahimsn.lib.PhoneNumberKit

class ProfileActivity : BaseActivity<ActivityProfileBinding>(), View.OnClickListener {
    private var path: String?=null
    private var phoneNumberKit : PhoneNumberKit? = null

    override fun getActivityBinding(): ActivityProfileBinding = ActivityProfileBinding.inflate(layoutInflater)
    override fun inits() {
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
    }
    override fun observer() {
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
            R.id.ivButton ->{ finish() }
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


    private var onScopeResultLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            path=cameraIntent(this,resultLauncher)
        }
    }

    private var onCameraLaucher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions->
        var permission=true
        permissions.entries.forEach {
            Log.e("DEBUG", "${it.key} = ${it.value}")
            if(!it.value){ permission=it.value }
        }
        when (permission){
            true -> {
                if (!checkStoragePermissions(this)) requestStoragePermission(this, onScopeResultLaucher = onScopeResultLaucher, onPermissionlaucher = onPermissionlaucher)
                else path=cameraIntent(this,resultLauncher)
            }
            else ->{ binding.root.snack("Please Allow Permission to access your camera"){} }
        }
    }

    private var onPermissionlaucher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        var permission=true
        permissions.entries.forEach {
            Log.e("DEBUG", "${it.key} = ${it.value}")
            if(!it.value){ permission=it.value }
        }
        when(permission){
            true ->{ path=cameraIntent(this,resultLauncher) }
            else ->{ binding.root.snack("Please Allow Permission to access your camera"){} }
        }
    }


    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            if(result.data!=null){
                path = getPath(this, Uri.parse(result?.data?.dataString))
            }

            binding.ivProfile.loadProfilePic(path,binding.progressBar)
        }


    }

}