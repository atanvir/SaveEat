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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantBottomSheet : BaseBottomSheet<BottomSheetRestaurantInfoBinding>(), View.OnClickListener {

    override fun getBottomSheetBinding(inflater: LayoutInflater, container: ViewGroup?): BottomSheetRestaurantInfoBinding = BottomSheetRestaurantInfoBinding.inflate(inflater,container,false)

    override fun init() {
        binding.model=arguments?.getParcelable("data")
    }

    override fun initCtrl() {
        binding.clFooter.rlDirection.setOnClickListener(this)
        binding.clFooter.rlPhoneCall.setOnClickListener(this)
        binding.clFooter.rlShareApp.setOnClickListener(this)
    }

    override fun observer() {}

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.rlShareApp -> {
                val sendIntent =  Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }

            R.id.rlDirection -> {
                CommonUtils.openGoogleMap(requireActivity(),(binding.clFooter.rlDirection.tag as String).split(",")[0].toDouble(),(binding.clFooter.rlDirection.tag as String).split(",")[1].toDouble())
            }

            R.id.rlPhoneCall ->{
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + binding.clFooter.tvPhoneCall.text.toString()))
                startActivity(intent)
            }
        }
    }
}