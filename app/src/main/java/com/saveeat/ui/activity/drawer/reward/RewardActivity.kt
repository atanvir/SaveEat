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
import com.saveeat.model.request.FAQModel
import com.saveeat.ui.adapter.content.FAQAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RewardActivity : BaseActivity<ActivityRewardBinding>(), View.OnClickListener {

    var list: MutableList<FAQModel?>? = ArrayList()
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
        binding.rvFaq.adapter=FAQAdapter(this,getFAQModel())
    }

    private fun getFAQModel(): MutableList<FAQModel?>? {
        var list: MutableList<FAQModel?>? = ArrayList()
        list?.add(FAQModel(title = "What happens if I don't make enough orders within 30 days?","Without 5 orders, those prizes cannont be won (Simple as that). But there's always another chance! Any order you make at ₹500 or higher after that will reset your progress & give you another 30 days to complete it."))
        list?.add(FAQModel(title = "Can I get multiple rewards and combine them?","Love your thinking, but sadly not. You'll need to redeem your prize before you can start chasing your next one."))
        list?.add(FAQModel(title = "How does planting a tree work?","Trees help clean the air we breathe, filter the water we drink, and provide habitat to over 80% of the world's terrestrial biodiversity. They're awsome! So we're working with the non-profit organisation One Tree Planted who will help us plant a tree every time you choose this reward. They have projects across the world, including India supporting global reforestation and every tree we can plant together will help that mission."))
        list?.add(FAQModel(title = "Can I pause my progress?","There's no way to pause progress, or extend the 30 day period. (Great tip though - if you're out of town, why not recue and share your order with a friend. It still counts, promise!)"))
        list?.add(FAQModel(title = "When do I choose the reward?","Once you've completed 30 day challenge, it's time to claim your reward. You'll have 30 days to choose your prize, so no time to waste!"))
        return list
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