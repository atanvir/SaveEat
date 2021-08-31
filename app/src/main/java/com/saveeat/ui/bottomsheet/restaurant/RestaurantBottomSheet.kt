package com.saveeat.ui.bottomsheet.restaurant

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saveeat.BuildConfig
import com.saveeat.R
import com.saveeat.base.BaseBottomSheet
import com.saveeat.databinding.BottomSheetRestaurantInfoBinding
import com.saveeat.model.response.saveeat.menu.RestaurantDetailModel
import com.saveeat.utils.application.CommonUtils
import com.saveeat.utils.extn.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantBottomSheet : BaseBottomSheet<BottomSheetRestaurantInfoBinding>(), View.OnClickListener {

    override fun getBottomSheetBinding(inflater: LayoutInflater, container: ViewGroup?): BottomSheetRestaurantInfoBinding = BottomSheetRestaurantInfoBinding.inflate(inflater,container,false)

    override fun init() {
        val data=arguments?.getParcelable<RestaurantDetailModel?>("data")
        data?.menu=false
        binding.model=data
    }

    override fun initCtrl() {
        binding.clFooter.rlDirection.setOnClickListener(this)
        binding.clFooter.tvPhoneCall.setOnClickListener(this)
        binding.clFooter.rlShareApp.setOnClickListener(this)
        binding.clFooter.tvCancel.setOnClickListener(this)
        binding.clFooter.tvWebsite.setOnClickListener(this)
    }

    override fun observer() {}

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.rlShareApp -> {
                dismiss()
                val sendIntent =  Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }

            R.id.tvWebsite ->{
                dismiss()
                try{
                val sendIntent =  Intent(Intent.ACTION_VIEW, Uri.parse(binding.clFooter.tvWebsite.tag as String))
                    startActivity(sendIntent)
                }catch (e: Exception){
                    requireActivity().toast("No url found!")
                }
            }

            R.id.rlDirection -> {
                dismiss()
                CommonUtils.openGoogleMap(requireActivity(),(binding.clFooter.rlDirection.tag as String).split(",")[0].toDouble(),(binding.clFooter.rlDirection.tag as String).split(",")[1].toDouble())
            }

            R.id.tvPhoneCall ->{
                dismiss()
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + binding.clFooter.tvPhoneCall.text.toString()))
                startActivity(intent)
            }

            R.id.tvCancel ->{
                dismiss()
            }
        }
    }
}