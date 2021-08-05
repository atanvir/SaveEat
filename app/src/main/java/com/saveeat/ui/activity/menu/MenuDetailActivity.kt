package com.saveeat.ui.activity.menu

import android.content.Intent
import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityMenuDetailBinding
import com.saveeat.ui.activity.main.MainActivity
import com.saveeat.ui.adapter.menu.MenuMultipleChoiceAdapter
import com.saveeat.utils.helper.AppBarStateChangeListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuDetailActivity : BaseActivity<ActivityMenuDetailBinding>(), View.OnClickListener {

    override fun getActivityBinding(): ActivityMenuDetailBinding = ActivityMenuDetailBinding.inflate(layoutInflater)

    override fun inits() {
        binding.clShadowButton.tvButtonLabel.text="Add to cart"
        binding.rvChoices.layoutManager=LinearLayoutManager(this)
        binding.rvChoices.adapter = MenuMultipleChoiceAdapter(this)
        binding.mp.paintFlags= binding.mp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
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
                    binding.tvDescription.visibility=View.INVISIBLE
                    binding.view.visibility=View.INVISIBLE
                    binding.tvToolbarHeader.visibility=View.VISIBLE
                    binding.ivRestroImage.visibility=View.VISIBLE

                } else if (State.IDLE == state) {
                    binding.tvDescription.visibility=View.VISIBLE
                    binding.view.visibility=View.VISIBLE
                    binding.tvToolbarHeader.visibility=View.INVISIBLE
                    binding.ivRestroImage.visibility=View.INVISIBLE
                }
            }
        })

    }

    override fun observer() {
    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.ivButton ->{ startActivity(Intent(this,MainActivity::class.java).putExtra("menu",true)) }
            R.id.ivPlus ->{
              var count =binding.clQuantity.tvQuantity.text.toString().toInt()
              count += 1
              binding.clQuantity.tvQuantity.text=count.toString()
            }

            R.id.ivMinus ->{
                var count =binding.clQuantity.tvQuantity.text.toString().toInt()
                if(count>0){
                count -= 1
                binding.clQuantity.tvQuantity.text=count.toString()
                }
            }
            R.id.clRestroDetail ->{ startActivity(Intent(this,MenuActivity::class.java)) }
            R.id.ivBack ->{ onBackPressed() }
        }
    }
}