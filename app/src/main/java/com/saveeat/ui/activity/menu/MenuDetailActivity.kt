package com.saveeat.ui.activity.menu

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityMenuBinding
import com.saveeat.databinding.ActivityMenuDetailBinding
import com.saveeat.ui.adapter.menu.MenuMultipleChoiceAdapter
import com.saveeat.utils.helper.AppBarStateChangeListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuDetailActivity : BaseActivity<ActivityMenuDetailBinding>() {

    override fun getActivityBinding(): ActivityMenuDetailBinding = ActivityMenuDetailBinding.inflate(layoutInflater)

    override fun inits() {
        binding.rvChoices.layoutManager=LinearLayoutManager(this)
        binding.rvChoices.adapter = MenuMultipleChoiceAdapter(this)
        binding.clShadowButton.tvButtonLabel.text="Add to cart"
        binding.mp.paintFlags= binding.mp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    override fun initCtrl() {
        binding.appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {

                if (State.COLLAPSED == state) {
                    binding.collapsingToolbarLayout.title="Poached eggs &amp; avocado\n" + "on toast"
                    binding.tvDescription.visibility=View.INVISIBLE
                    binding.view.visibility=View.INVISIBLE

                } else if (State.IDLE == state) {

                    binding.collapsingToolbarLayout.title=""
                    binding.tvDescription.visibility=View.VISIBLE
                    binding.view.visibility=View.VISIBLE
                }
            }
        })
    }

    override fun observer() {
    }
}