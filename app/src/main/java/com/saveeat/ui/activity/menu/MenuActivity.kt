package com.saveeat.ui.activity.menu

import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityMenuBinding
import com.saveeat.ui.adapter.menu.MenuCategoryAdapter
import com.saveeat.ui.adapter.restaurant.RestaurantAdapter
import com.saveeat.utils.application.StaticDataHelper
import com.saveeat.utils.helper.AppBarStateChangeListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuActivity : BaseActivity<ActivityMenuBinding>(), View.OnClickListener, (Int) -> Unit {
    override fun getActivityBinding(): ActivityMenuBinding = ActivityMenuBinding.inflate(layoutInflater)

    override fun inits() {
        binding.rvMenuCategories.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.rvMenuCategories.adapter=MenuCategoryAdapter(this,StaticDataHelper.getMenuCategory(),this)

        binding.rvProducts.layoutManager=GridLayoutManager(this,2)
        binding.rvProducts.adapter= RestaurantAdapter(this,StaticDataHelper.menuData(),true)
    }

    override fun initCtrl() {

        binding.cpType.setOnClickListener(this)
        binding.cpType.setOnCloseIconClickListener {
            binding.cpType.isCloseIconVisible=false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.cpType.chipBackgroundColor=getColorStateList(R.color.white)
                binding.cpType.closeIconTint=getColorStateList(R.color.black)
            }
            binding.cpType.setTextColor(ContextCompat.getColor(this,R.color.black))
        }
        binding.ivBack.setOnClickListener(this)
    }

    override fun observer() {
//        binding.appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener())

        binding.appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {

                if (State.COLLAPSED == state) {
                    binding.cvMain.visibility=View.INVISIBLE
                    binding.tvToolbarHeader.visibility=View.VISIBLE
                    binding.ivRestroImage.visibility=View.VISIBLE

                } else if (State.IDLE == state) {
                    binding.cvMain.visibility=View.VISIBLE
                    binding.tvToolbarHeader.visibility=View.INVISIBLE
                    binding.ivRestroImage.visibility=View.INVISIBLE
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivBack ->{ onBackPressed() }
            R.id.cpType ->{
                binding.cpType.isCloseIconVisible=true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.cpType.chipBackgroundColor=this.getColorStateList(R.color.app_theme)
                    binding.cpType.closeIconTint=this.getColorStateList(R.color.white)
                }
                binding.cpType.setTextColor(ContextCompat.getColor(this,R.color.white))
            }
        }
    }

    override fun invoke(p1: Int) {
        binding.rvMenuCategories.smoothScrollToPosition(p1)
        binding.nestedSvProduct.scrollTo(0,0)
        (binding.rvProducts.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
        when (p1) {
            0 -> binding.rvProducts.adapter= RestaurantAdapter(this,StaticDataHelper.menuData(),true)
            1 -> binding.rvProducts.adapter= RestaurantAdapter(this,StaticDataHelper.menuData2(),true)
            2 -> binding.rvProducts.adapter= RestaurantAdapter(this,StaticDataHelper.menuData3(),true)
            3 -> binding.rvProducts.adapter= RestaurantAdapter(this,StaticDataHelper.menuData4(),true)
            4 -> binding.rvProducts.adapter= RestaurantAdapter(this,StaticDataHelper.menuData5(),true)
            else -> binding.rvProducts.adapter= RestaurantAdapter(this,StaticDataHelper.menuData(),true)
        }
    }


}