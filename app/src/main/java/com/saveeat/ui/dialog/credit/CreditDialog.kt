package com.saveeat.ui.dialog.credit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saveeat.R
import com.saveeat.base.BaseDialog
import com.saveeat.databinding.DialogCreditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreditDialog : BaseDialog<DialogCreditBinding>(), View.OnClickListener {
    override fun getDialogBinding(inflater: LayoutInflater, container: ViewGroup?): DialogCreditBinding = DialogCreditBinding.inflate(inflater,container,false)

    override fun init() {
        binding.clShadowButton.tvButtonLabel.text=getString(R.string.send)
    }

    override fun initCtrl() {
        binding.ivRemove.setOnClickListener(this)
        binding.clShadowButton.ivButton.setOnClickListener(this)

    }

    override fun observer() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivRemove ->{ dismiss() }
            R.id.ivButton ->{ dismiss() }
        }
    }
}