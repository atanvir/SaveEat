package com.saveeat.ui.activity.rating

import android.content.Intent
import android.view.View
import android.widget.RatingBar
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityRatingBinding
import com.saveeat.model.request.rating.RatingModel
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.repository.cache.PrefrencesHelper
import com.saveeat.utils.application.CommonUtils
import com.saveeat.utils.application.CommonUtils.buttonLoader
import com.saveeat.utils.application.CommonUtils.toolbar
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.Resource
import com.saveeat.utils.extn.snack
import com.saveeat.utils.extn.text
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RatingActivity : BaseActivity<ActivityRatingBinding>(), RatingBar.OnRatingBarChangeListener, View.OnClickListener {
   private val viewModel: RatingViewModel by viewModels()

    override fun getActivityBinding(): ActivityRatingBinding = ActivityRatingBinding.inflate(layoutInflater)

    override fun inits() {
        toolbar(this)
        binding.clShadowButton.tvButtonLabel.text=getString(R.string.submit)
    }

    override fun initCtrl() {
        binding.rbReview.onRatingBarChangeListener=this
        binding.clShadowButton.clMainShadow.setOnClickListener(this)
    }

    override fun observer() {
        lifecycleScope.launch {
            viewModel.ratingByUser.observe(this@RatingActivity,{
                buttonLoader(binding.clShadowButton,false)
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            val intent= Intent()
                            intent.putExtra("message",it.value?.message)
                            setResult(RESULT_OK,intent)
                            finish()
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0){ ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> {
                        ErrorUtil.handlerGeneralError(binding.root, it.throwable!!)
                    }
                }
            })
        }
    }

    override fun onRatingChanged(ratingBar: RatingBar?, rating: Float, fromUser: Boolean) {
        when(rating) {

            0.5f,1.0f -> { binding.ivHolder.setAnimation("emoji_rating_one.json")
                binding.ivHolder.playAnimation() }

            1.5f,2.0f -> { binding.ivHolder.setAnimation("emoji_rating_two.json")
                binding.ivHolder.playAnimation() }

            2.5f,3.0f -> { binding.ivHolder.setAnimation("emoji_rating_three.json")
                binding.ivHolder.playAnimation() }

            3.5f,4.0f,4.5f -> { binding.ivHolder.setAnimation("emoji_rating_four.json")
                binding.ivHolder.playAnimation() }

            5.0f -> { binding.ivHolder.setAnimation("emoji_rating_five.json")
                binding.ivHolder.playAnimation() }

            else -> { binding.ivHolder.setAnimation("emoji_rating_one.json")
                binding.ivHolder.playAnimation() }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.clShadowButton ->{
                buttonLoader(binding.clShadowButton,true)
                if(checkValidation()){
                    viewModel.ratingByUser(RatingModel(orderId = intent.getStringExtra("_id"),
                                                       rating=binding.rbReview.rating.toString(),
                                                       review = binding.tieReview.text(),
                                                       token = PrefrencesHelper.getPrefrenceStringValue(this, PreferenceKeyConstants.jwtToken),
                                                       userId= PrefrencesHelper.getPrefrenceStringValue(this, PreferenceKeyConstants._id)))
                }else{
                    buttonLoader(binding.clShadowButton,false)
                }
            }
        }
    }

    private fun checkValidation(): Boolean {
        var ret=false

        if(binding?.rbReview?.rating==0f){
            ret=false
            binding.root.snack("Please add some rating"){}
        }

        return ret
    }
}