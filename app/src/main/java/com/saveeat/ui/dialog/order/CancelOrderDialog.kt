package com.saveeat.ui.dialog.order

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.saveeat.R
import com.saveeat.base.BaseDialog
import com.saveeat.databinding.DialogCancelOrderBinding
import com.saveeat.databinding.DialogCreditBinding
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CancelOrderDialog(var listner: ()->Unit) : BaseDialog<DialogCancelOrderBinding>(), View.OnClickListener {

    override fun getDialogBinding(inflater: LayoutInflater, container: ViewGroup?): DialogCancelOrderBinding = DialogCancelOrderBinding.inflate(inflater,container,false)

    override fun init() {
        binding.clShadowButton.tvButtonLabel.text="Confirm"
    }

    override fun initCtrl() {
        binding.clShadowButton.ivButton.setOnClickListener(this)
        binding.ivRemove.setOnClickListener(this)
    }

    override fun observer() {
  
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivButton ->{
                dismiss()
                listner?.invoke()
            }

            R.id.ivRemove ->{ dismiss() }

        }
    }
}