package com.saveeat.ui.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saveeat.R
import com.saveeat.base.BaseDialog
import com.saveeat.databinding.DialogRewardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RewardDialog  : BaseDialog<DialogRewardBinding>(), View.OnClickListener {
    override fun getDialogBinding(inflater: LayoutInflater, container: ViewGroup?): DialogRewardBinding = DialogRewardBinding.inflate(inflater,container,false)

    override fun init() {
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