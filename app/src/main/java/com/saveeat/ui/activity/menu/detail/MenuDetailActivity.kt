package com.saveeat.ui.activity.menu.detail

import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityMenuDetailBinding
import com.saveeat.model.request.cart.AddToCartModel
import com.saveeat.model.request.cart.ChoiceModel
import com.saveeat.model.request.cart.UpdateCartModel
import com.saveeat.model.request.menu.MenuItemDetailModel
import com.saveeat.model.response.saveeat.cart.ProductDataModel
import com.saveeat.model.response.saveeat.menu.MenuExtraModel
import com.saveeat.repository.cache.PreferenceKeyConstants._id
import com.saveeat.repository.cache.PreferenceKeyConstants.jwtToken
import com.saveeat.repository.cache.PreferenceKeyConstants.latitude
import com.saveeat.repository.cache.PreferenceKeyConstants.longitude
import com.saveeat.repository.cache.PrefrencesHelper
import com.saveeat.ui.activity.menu.menu.MenuActivity
import com.saveeat.ui.adapter.menu.MenuMultipleChoiceAdapter
import com.saveeat.utils.application.CommonUtils.buttonLoader
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.Resource
import com.saveeat.utils.helper.AppBarStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class MenuDetailActivity : BaseActivity<ActivityMenuDetailBinding>(), View.OnClickListener, (Int,Boolean) -> Unit {

    private var extraItemPrice: Double?=0.0
    private var itemPrice: Double?=0.0
    private var quantity: Int?=0
    private var requirement: String?=""
    private var extraList:MutableList<MenuExtraModel?>? = null
    private val viewModel: MenuDetailViewModel by viewModels()

    override fun getActivityBinding(): ActivityMenuDetailBinding = ActivityMenuDetailBinding.inflate(layoutInflater)

    override fun inits() {
        binding.clShadowButton.tvButtonLabel.text=""
        binding.mp.paintFlags= binding.mp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        viewModel.getItemDetail(MenuItemDetailModel(latitude= PrefrencesHelper.getPrefrenceStringValue(this, latitude),
                                                    longitude= PrefrencesHelper.getPrefrenceStringValue(this, longitude),
                                                    userId = PrefrencesHelper.getPrefrenceStringValue(this,_id),
                                                    itemId=intent?.getStringExtra("_id")?:"",
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
                            extraList=it.value?.data?.extra
                            itemPrice=binding?.model?.offeredPrice
                            quantity=getCurrentQuantity(binding?.model?.cartData?.productData)
                            if(it.value?.data?.extra?.isNotEmpty()==true) binding.tvLabelExtra.visibility=View.VISIBLE
                            binding.rvChoices.layoutManager=LinearLayoutManager(this@MenuDetailActivity)
                            binding.rvChoices.adapter=MenuMultipleChoiceAdapter(this@MenuDetailActivity,getSelectedExtra(),this@MenuDetailActivity)
                            updateQuantity(0)

                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> { ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })

            viewModel.addToCart.observe(this@MenuDetailActivity,{
                buttonLoader(binding.clShadowButton,false)
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) finish()
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> { ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })


            viewModel.updateCart.observe(this@MenuDetailActivity,{
                buttonLoader(binding.clShadowButton,false)
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) finish()
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> { ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })
        }
    }

    private fun getSelectedExtra(): MutableList<MenuExtraModel?>? {
        if(extraList!=null){
        for(i in extraList?.indices!!){
            if(binding?.model?.cartData?.productData!=null){
           for(j in binding?.model?.cartData?.productData?.indices!!){
               if(binding?.model?.cartData?.productData?.get(j)?.choice!=null){
              for(k in  binding?.model?.cartData?.productData?.get(j)?.choice?.indices!!){
                  if(binding?.model?.cartData?.productData?.get(j)?.choice?.get(k)?.id?.equals(extraList?.get(i)?._id)==true){
                      extraList?.get(i)?.check=true
                      extraItemPrice=extraList?.get(i)?.price
                  }
              }
               }
           }
        }
        }
        }
        return extraList
    }

    private fun getCurrentQuantity(list: List<ProductDataModel?>?): Int? {
        var quantity: Int?=0
        if(list!=null){
        for(i in list?.indices!!){
            if(list?.get(i)?.productId?.equals(intent?.getStringExtra("_id")?:"",ignoreCase = true)==true){
                quantity=list?.get(i)?.quantity
                requirement=list?.get(i)?.requirement
                break
            }
        }
        }
     return quantity
    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.ivButton -> {
                buttonLoader(binding.clShadowButton, true)
                if(checkValidation()){
                    if (binding?.model?.cartData != null) viewModel.updateCart(getUpdateCartModel())
                    else viewModel.addToCart(getAddToCartModel())
                }else buttonLoader(binding.clShadowButton, false)

            }
            R.id.ivPlus -> { updateQuantity(+1) }

            R.id.ivMinus -> { if(quantity?:0>0) updateQuantity(-1) }
            R.id.clRestroDetail -> {
                val intent=Intent(this,MenuActivity::class.java)
                intent.putExtra("_id",binding?.model?.menuId)
                intent.putExtra("type", KeyConstants.RESTAURANT)
                startActivity(intent)
            }
            R.id.ivBack ->{ onBackPressed() }
        }
    }

    private fun getUpdateCartModel(): UpdateCartModel? {
        return UpdateCartModel(itemId= intent?.getStringExtra("_id")?:"",
                               cartId = binding?.model?.cartData?._id?:"",
                               quantity =quantity,
                               choice = getSelectedChoices(),
                               requirement=requirement,
                               token = PrefrencesHelper.getPrefrenceStringValue(this, jwtToken),
                               userId= PrefrencesHelper.getPrefrenceStringValue(this, _id))
    }

    private fun getAddToCartModel(): AddToCartModel? {
        return AddToCartModel(restroId=binding?.model?.restroData?._id,
                              productId = intent?.getStringExtra("_id")?:"",
                              quantity =quantity,
                              choice = getSelectedChoices(),
                              token = PrefrencesHelper.getPrefrenceStringValue(this, jwtToken),
                              userId= PrefrencesHelper.getPrefrenceStringValue(this, _id))
    }

    private fun updateQuantity(addOn: Int) {
        quantity = quantity?.plus(addOn)
        binding.clQuantity.tvQuantity.text = quantity.toString()
        val totalPrice= ((itemPrice?.plus(extraItemPrice!!))?.times(quantity ?: 0)!!).roundToInt()
        if(binding?.model?.cartData==null) binding.clShadowButton.tvButtonLabel.text="Add to cart (${getString(R.string.price,totalPrice.toString())})"
        else binding.clShadowButton.tvButtonLabel.text="Update cart (${getString(R.string.price,totalPrice.toString())})"
    }


    override fun invoke(position: Int,check : Boolean) {
        extraList?.get(position)?.check=check
        extraItemPrice = if(extraList?.get(position)?.check == true) extraItemPrice?.plus(extraList?.get(position)?.price?:0.0)
                         else extraItemPrice?.minus(extraList?.get(position)?.price?:0.0)

        updateQuantity(0)
        binding.rvChoices.adapter?.notifyItemChanged(position)
    }

    private fun getSelectedChoices(): MutableList<ChoiceModel?>? {
        val choiceList : MutableList<ChoiceModel?>? = ArrayList()
        for (i in extraList?.indices!!){
            if(extraList?.get(i)?.check==true){
                choiceList?.add(ChoiceModel(extraList?.get(i)?._id,extraList?.get(i)?.foodName,extraList?.get(i)?.price,quantity))
            }
        }
        return choiceList
    }

    private fun checkValidation(): Boolean {
        var ret=true

        if(quantity?:0==0){
            ret=false
            ErrorUtil.snackView(binding.root,"Please add some quantity")
        }
        return ret
    }

}