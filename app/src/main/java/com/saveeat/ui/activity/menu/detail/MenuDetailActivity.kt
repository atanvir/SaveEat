package com.saveeat.ui.activity.menu.detail

import android.content.Intent
import android.graphics.Paint
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityMenuDetailBinding
import com.saveeat.model.request.menu.MenuItemDetailModel
import com.saveeat.repository.cache.PreferenceKeyConstants._id
import com.saveeat.repository.cache.PreferenceKeyConstants.jwtToken
import com.saveeat.repository.cache.PreferenceKeyConstants.latitude
import com.saveeat.repository.cache.PreferenceKeyConstants.longitude
import com.saveeat.repository.cache.PrefrencesHelper
import com.saveeat.ui.activity.main.MainActivity
import com.saveeat.ui.activity.menu.menu.MenuActivity
import com.saveeat.ui.adapter.menu.MenuMultipleChoiceAdapter
import com.saveeat.ui.adapter.restaurant.SavedRestaurantAdapter
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.Resource
import com.saveeat.utils.helper.AppBarStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MenuDetailActivity : BaseActivity<ActivityMenuDetailBinding>(), View.OnClickListener {
    private val viewModel: MenuDetailViewModel by viewModels()

    override fun getActivityBinding(): ActivityMenuDetailBinding = ActivityMenuDetailBinding.inflate(layoutInflater)

    override fun inits() {
        binding.clShadowButton.tvButtonLabel.text=getString(R.string.add_to_cart)
        binding.mp.paintFlags= binding.mp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        viewModel.getItemDetail(MenuItemDetailModel(latitude= PrefrencesHelper.getPrefrenceStringValue(this, latitude),
                                                    longitude= PrefrencesHelper.getPrefrenceStringValue(this, longitude),
                                                    userId = PrefrencesHelper.getPrefrenceStringValue(this,_id),
                                                    itemId=intent?.getStringExtra("_id")?:""/*"611e2e7987239f6b633c65b6"*/,
                                                    token = PrefrencesHelper.getPrefrenceStringValue(this, jwtToken)))
    }

    override fun initCtrl() {
        binding.clRestroDetail.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
        binding.clShadowButton.ivButton.setOnClickListener(this)
        binding.clQuantity.ivMinus.setOnClickListener(this)
        binding.clQuantity.ivPlus.setOnClickListener(this)
        binding.appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                if (State.COLLAPSED == state) {
                    binding.tvTitle.visibility=View.INVISIBLE
                    binding.ivType.visibility=View.INVISIBLE
                    binding.tvDescription.visibility=View.INVISIBLE
                    binding.view.visibility=View.INVISIBLE
                    binding.tvToolbarHeader.visibility=View.VISIBLE
                    binding.ivRestroImage.visibility=View.VISIBLE

                } else if (State.IDLE == state) {
                    binding.tvTitle.visibility=View.VISIBLE
                    binding.ivType.visibility=View.VISIBLE

                    binding.tvDescription.visibility=View.VISIBLE
                    binding.view.visibility=View.VISIBLE
                    binding.tvToolbarHeader.visibility=View.INVISIBLE
                    binding.ivRestroImage.visibility=View.INVISIBLE
                }
            }
        })
    }

    override fun observer() {
        lifecycleScope.launch {
            viewModel.getItemDetail.observe(this@MenuDetailActivity,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            binding.model=it.value?.data
                            if(it.value?.data?.extra?.isNotEmpty()==true) binding.tvLabelExtra.visibility=View.VISIBLE
                            binding.rvChoices.layoutManager=LinearLayoutManager(this@MenuDetailActivity)
                            binding.rvChoices.adapter=MenuMultipleChoiceAdapter(this@MenuDetailActivity,it.value?.data?.extra)
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> { ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })
        }
    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.ivButton -> { startActivity(Intent(this,MainActivity::class.java).putExtra("menu",true)) }
            R.id.ivPlus -> {
              var count =binding.clQuantity.tvQuantity.text.toString().toInt()
              count += 1
              binding.clQuantity.tvQuantity.text=count.toString()
            }

            R.id.ivMinus -> {
                var count =binding.clQuantity.tvQuantity.text.toString().toInt()
                if(count>0){
                count -= 1
                binding.clQuantity.tvQuantity.text=count.toString()
                }
            }
            R.id.clRestroDetail -> {
                val intent=Intent(this,MenuActivity::class.java)
                intent.putExtra("_id",binding?.model?.menuId)
                intent.putExtra("type", KeyConstants.RESTAURANT)
                startActivity(intent)
            }
            R.id.ivBack ->{ onBackPressed() }
        }
    }
}