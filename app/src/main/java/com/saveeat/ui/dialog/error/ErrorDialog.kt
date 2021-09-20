package com.saveeat.ui.dialog.error

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saveeat.R
import com.saveeat.base.BaseDialog
import com.saveeat.databinding.DialogErrorBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ErrorDialog : BaseDialog<DialogErrorBinding>(), View.OnClickListener {
    override fun getDialogBinding(inflater: LayoutInflater, container: ViewGroup?): DialogErrorBinding = DialogErrorBinding.inflate(inflater,container,false)

    override fun init() {

        binding.tvMessage.text=arguments?.getString("message")
        if(arguments?.getString("message").equals(getString(R.string.clear_choices))){
            binding.btnOk.text = "No"
            binding.btnYes.visibility=View.VISIBLE
        }
    }

    override fun initCtrl() {
        binding.btnOk.setOnClickListener(this)
        binding.btnYes.setOnClickListener(this)
    }

    override fun observer() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnOk->{ dismiss() }
            R.id.btnYes->{
            dismiss()
            requireActivity().finish()
            }
        }
    }

}