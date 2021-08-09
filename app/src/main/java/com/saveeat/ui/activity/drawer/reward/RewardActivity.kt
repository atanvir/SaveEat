package com.saveeat.ui.activity.drawer.reward

import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityRewardBinding
import com.saveeat.ui.adapter.content.FAQAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RewardActivity : BaseActivity<ActivityRewardBinding>(), View.OnClickListener {
    override fun getActivityBinding(): ActivityRewardBinding = ActivityRewardBinding.inflate(layoutInflater)

    override fun inits() {

        binding.clMainHeader.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val bottomSheet = findViewById<CoordinatorLayout>(R.id.clBottomSheet)
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.peekHeight = resources.displayMetrics.heightPixels-(binding.clMainHeader.height+20)
                binding.clMainHeader.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        binding.rvFaq.layoutManager=LinearLayoutManager(this)
        binding.rvFaq.adapter=FAQAdapter(this)
    }

    override fun initCtrl() {
        binding.ivClose.setOnClickListener(this)
    }

    override fun observer() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivClose ->{ onBackPressed() }
        }
    }
}