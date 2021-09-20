package com.saveeat.ui.dialog.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.saveeat.R
import com.saveeat.base.BaseDialog
import com.saveeat.databinding.DialogRequirementBinding
import com.saveeat.model.response.saveeat.cart.ProductDataModel
import com.saveeat.ui.adapter.cart.CartItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequirementDialog(var model : ProductDataModel?,var listner: CartItemAdapter.setOnClickListner,var parentPostion : Int?,var adapterPosition: Int?): BaseDialog<DialogRequirementBinding>(), View.OnClickListener {
    override fun getDialogBinding(inflater: LayoutInflater, container: ViewGroup?): DialogRequirementBinding = DialogRequirementBinding.inflate(inflater, container, false)

    override fun init() {
        binding.data=model
        binding.clShadowButton.tvButtonLabel.text = getString(R.string.done)
    }

    override fun initCtrl() {
        binding.ivRemove.setOnClickListener(this)
        binding.clShadowButton.ivButton.setOnClickListener(this)
    }

    override fun observer() {
        binding.tieMessage.addTextChangedListener {
            binding.tilMessage.isErrorEnabled=false
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivRemove -> { dismiss() }
            R.id.ivButton -> { if(checkValidation()) {
                                    dismiss()
                                    listner?.updateCart(parentPostion,adapterPosition,0) }
            }
        }
    }

    private fun checkValidation(): Boolean {
        var ret=true

        if(binding?.data?.requirement?.isEmpty()==true) {
            ret=false
            binding.tilMessage.isErrorEnabled=true
            binding.tilMessage.error=getString(R.string.please_enter_valid_phone_number)
        }

        return ret
    }
}
