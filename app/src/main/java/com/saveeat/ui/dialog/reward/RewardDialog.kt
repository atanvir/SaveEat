package com.saveeat.ui.dialog.reward

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saveeat.R
import com.saveeat.base.BaseDialog
import com.saveeat.databinding.DialogRewardBinding
import com.saveeat.model.response.saveeat.badge.BadgeData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RewardDialog(var data: BadgeData?)  : BaseDialog<DialogRewardBinding>(), View.OnClickListener {
    override fun getDialogBinding(inflater: LayoutInflater, container: ViewGroup?): DialogRewardBinding = DialogRewardBinding.inflate(inflater,container,false)

    override fun init() {
        binding.data=data
        if(binding.data?.badgeCount?:0==0) binding.ivLogo.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f)})

    }

    override fun initCtrl() {
        binding.ivRemove.setOnClickListener(this)
        binding.btnAwesome.setOnClickListener(this)
    }

    override fun observer() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivRemove ->{ dismiss() }
            R.id.btnAwesome ->{ dismiss() }
        }
    }
}