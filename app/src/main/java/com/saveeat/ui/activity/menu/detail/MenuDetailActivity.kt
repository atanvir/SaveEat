package com.saveeat.ui.activity.menu.detail

import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
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
import com.saveeat.model.response.saveeat.menu.MenuCategoryModel
import com.saveeat.model.response.saveeat.menu.MenuExtraModel
import com.saveeat.repository.cache.PreferenceKeyConstants._id
import com.saveeat.repository.cache.PreferenceKeyConstants.jwtToken
import com.saveeat.repository.cache.PreferenceKeyConstants.latitude
import com.saveeat.repository.cache.PreferenceKeyConstants.longitude
import com.saveeat.repository.cache.PrefrencesHelper
import com.saveeat.ui.activity.menu.menu.MenuActivity
import com.saveeat.ui.adapter.menu.MenuCustomizationAdapter
import com.saveeat.ui.adapter.menu.MenuCustomizationListner
import com.saveeat.utils.application.CommonUtils
import com.saveeat.utils.application.CommonUtils.buttonLoader
import com.saveeat.utils.application.CommonUtils.getProductType
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.Resource
import com.saveeat.utils.extn.roundOffDecimal
import com.saveeat.utils.extn.snack
import com.saveeat.utils.helper.AppBarStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

@AndroidEntryPoint
class MenuDetailActivity : BaseActivity<ActivityMenuDetailBinding>(), View.OnClickListener, MenuCustomizationListner {

    private var menuItemPrice: Double?=0.0
    private var price: Double?=0.0
    private var quantity: Int?=0
    private var actualPrice: Double?=0.0

    private val viewModel: MenuDetailViewModel by viewModels()

    private var requirement: String?=""


    private var list: MutableList<MenuCategoryModel?>?=null
    private val singleChoiceMap : MutableMap<Int,Double> = HashMap()
    private val multipleChoiceMap : MutableMap<Int,Double> = HashMap()


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
                    binding.ivBack.visibility=View.VISIBLE

                    binding.tvToolbarHeader.visibility=View.VISIBLE
                    binding.ivRestroImage.visibility=View.VISIBLE

                } else if (State.IDLE == state) {
                    binding.ivBack.visibility=View.INVISIBLE
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
                            Log.e("sellingStatus",binding?.model?.sellingStatus.toString())
                            Log.e("price",binding?.model?.price.toString())
                            loadPreviousSelectedChoices()
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

    private fun loadPreviousSelectedChoices() {
        if(binding?.model?.cartData!=null){
            if(binding?.model?.cartData?.choice?.isNullOrEmpty() != true && binding?.model?.category?.isNullOrEmpty() != true){
                if(binding?.model?.category!=null){
                    if(binding?.model?.cartData?.productData!=null){
                        for (k in binding?.model?.cartData?.productData?.indices!!){
                            if(binding?.model?.cartData?.productData?.get(k)?.choice!=null){
                                for(l in binding?.model?.cartData?.productData?.get(k)?.choice?.indices!!){
                                    for(i in binding?.model?.category?.indices!!){
                                        if(binding?.model?.category?.get(i)?.choice!=null){
                                            for(j in binding?.model?.category?.get(i)?.choice?.indices!!){
                                                if(binding?.model?.cartData?.productData?.get(k)?.choice?.get(l)?.categoryId.equals(binding?.model?.category?.get(i)?._id,ignoreCase = true)){
                                                    if(binding?.model?.cartData?.productData?.get(k)?.choice?.get(l)?.choiceId?.equals(binding?.model?.category?.get(i)?.choice?.get(j)?.choiceId,ignoreCase = true)==true){
                                                        binding?.model?.category?.get(i)?.choice?.get(j)?.check=true
                                                        binding?.model?.category?.get(i)?.count=(binding?.model?.category?.get(i)?.count?:0)+1
                                                        menuItemPrice=menuItemPrice?.plus(binding?.model?.cartData?.productData?.get(k)?.choice?.get(l)?.price?.toDouble()?.roundOffDecimal()?:0.0)?:0.0
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        list=binding?.model?.category
        if(binding?.model?.sellingStatus==false) actualPrice=binding?.model?.price
        else actualPrice=binding?.model?.offeredPrice
        quantity=getCurrentQuantity(binding?.model?.cartData?.productData)
        binding.rvChoices.layoutManager=LinearLayoutManager(this@MenuDetailActivity)
        binding.rvChoices.adapter=MenuCustomizationAdapter(this@MenuDetailActivity,list,this@MenuDetailActivity)
        updateQuantity(0)

//        CoroutineScope(Dispatchers.Main).launch {
//            CoroutineScope(Dispatchers.IO).launch{
//
//            }
//            CoroutineScope(Dispatchers.Main).launch {
//
//            }
//        }

    }

    private fun allPossibleCombination(isUIUpdate: Boolean) : Boolean {
        var ret=true
        var count=0
        if(list!=null){
        for(i in list?.indices!!){
            count=0
            if(list?.get(i)?.choice!=null) {
                for (j in list?.get(i)?.choice?.indices!!) {
                    if (list?.get(i)?.choice?.get(j)?.check == true) count += 1
                    if (j == (list?.get(i)?.choice?.size ?: 1) - 1) {
                        if (list?.get(i)?.min ?: 0 > 0) {
                            if (count < list?.get(i)?.min ?: 0 || count == 0) {
                                if (isUIUpdate) {
                                    list?.get(i)?.error = true
//                                    binding.rvChoices.adapter?.notifyItemChanged(i, list?.get(i))
                                    CommonUtils.showDialog(this@MenuDetailActivity,"You can select "+list?.get(i)?.categoryName+" "+ CommonUtils.calculateCombinationValidation(list?.get(i)?.min,list?.get(i)?.max))
                                    ret = false
                                    break
                                }
                                ret = false

                            }
                        }
                    }
                }
            }
        }
        }

        if(ret && price==0.0){
            ret=false
            if(isUIUpdate){
               binding.root.snack("Please add choose any combination or optional"){}
            }
        }

        if(ret && quantity==0){
            ret=false
        }


        return ret
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
                    if(allPossibleCombination(true)){
                    if (binding?.model?.cartData != null) viewModel.updateCart(getUpdateCartModel())
                    else viewModel.addToCart(getAddToCartModel())
                    }else{ buttonLoader(binding.clShadowButton, false) }
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
                               userId= PrefrencesHelper.getPrefrenceStringValue(this, _id),
                               type = getProductType(binding?.model?.sellingStatus))
    }

    private fun getAddToCartModel(): AddToCartModel? {
        return AddToCartModel(restroId=binding?.model?.restroData?._id,
                              productId = intent?.getStringExtra("_id")?:"",
                              quantity =quantity,
                              choice = getSelectedChoices(),
                              token = PrefrencesHelper.getPrefrenceStringValue(this, jwtToken),
                              userId= PrefrencesHelper.getPrefrenceStringValue(this, _id),
                              type=getProductType(binding?.model?.sellingStatus))
    }



    private fun updateQuantity(addOn: Int) {
        quantity = quantity?.plus(addOn)
        binding.clQuantity.tvQuantity.text = quantity.toString()
        updatePrice()
    }



    private fun getSelectedChoices(): MutableList<ChoiceModel?>? {
        val choiceList: MutableList<ChoiceModel?>? = ArrayList()
        if(list!=null){
        for (i in list?.indices!!) {
            if (list?.get(i)?.choice != null) {
                for (j in list?.get(i)?.choice?.indices!!) {
                    if (list?.get(i)?.choice?.get(j)?.check == true) {
                        choiceList?.add(
                            com.saveeat.model.request.cart.ChoiceModel(categoryId = list?.get(i)?._id ?: "",
                                                                        choiceId = list?.get(i)?.choice?.get(j)?.choiceId ?: "",
                                                                        foodName = list?.get(i)?.choice?.get(j)?.foodName ?: "",
                                                                        price =  list?.get(i)?.choice?.get(j)?.price,
                                                                        last =  list?.get(i)?.choice?.get(j)?.last?:false,
                                                                        check =  list?.get(i)?.choice?.get(j)?.check?:false,
                                                                        quantity = 1)
                        )
                    }
                }
            }
        }
        }
       return choiceList
    }

    private fun checkValidation(): Boolean {
        var ret=true


        if(binding?.model?.sellingStatus==true && binding?.model?.leftQuantity?:0L<=0){
            ret=false
            ErrorUtil.snackView(binding.root,"Nothing left for today")
        }
        else if(quantity?:0==0){
            ret=false
            ErrorUtil.snackView(binding.root,"Please add some quantity")
        }



        return ret
    }




    override fun onMenuSingleItemClick(position: Int?, data: MenuCategoryModel?) {
        if(list?.get(list?.indexOf(data)?:-1)?.choice?.get(position?:0)?.check==true){

            // If Single Choice Already Added
            if(checkPreviousValue(list?.indexOf(data)?:-1)) {
                menuItemPrice=menuItemPrice?.minus(singleChoiceMap.get(list?.indexOf(data)?:0)!!)
                singleChoiceMap.remove(list?.indexOf(data)?:0)
            }

            menuItemPrice=menuItemPrice?.plus(list?.get(list?.indexOf(data)?:-1)?.choice?.get(position?:0)?.price?:0.0)
            singleChoiceMap.put(list?.indexOf(data)?:0,list?.get(list?.indexOf(data)?:-1)?.choice?.get(position?:0)?.price?:0.0)

        }
        else{
            singleChoiceMap.remove(list?.indexOf(data)?:0)
            menuItemPrice=menuItemPrice?.minus(list?.get(list?.indexOf(data)?:-1)?.choice?.get(position?:0)?.price?:0.0)
        }
        updateCustomize(list?.indexOf(data)?:-1)
    }

    override fun onMenuMultipleItemClick(position: Int?, data: MenuCategoryModel?) {
        if(list?.get(list?.indexOf(data)?:-1)?.choice?.get(position?:0)?.check==true){
            menuItemPrice= menuItemPrice?.plus(list?.get(list?.indexOf(data)?:-1)?.choice?.get(position?:0)?.price?:0.0)
            multipleChoiceMap.put(list?.indexOf(data)?:0,list?.get(list?.indexOf(data)?:-1)?.choice?.get(position?:0)?.price?:0.0)
        }
        else{
            menuItemPrice=menuItemPrice?.minus(list?.get(list?.indexOf(data)?:-1)?.choice?.get(position?:0)?.price?:0.0)
            multipleChoiceMap.remove(list?.indexOf(data)?:0)
        }
        updateCustomize(list?.indexOf(data)?:-1)
    }

    private fun updateCustomize(position: Int) {
        updatePrice()

        if(list?.get(position)?.error==true) list?.get(position)?.error=false
        binding.rvChoices.adapter?.notifyItemChanged(position,list?.get(position))
    }


    private fun updatePrice() {
        price=actualPrice?.times(quantity?:0)?.plus(menuItemPrice!!)

//        price= ((actualPrice?.plus(menuItemPrice!!))?.times(quantity ?: 0)!!)

        try{

//            if(binding.model.sellingStatus){
//
//            }
            if(binding?.model?.sellingStatus==true && binding.model?.leftQuantity?:0<=0){
                binding.clShadowButton.tvButtonLabel.text="Nothing Left"
                binding.clShadowButton.ivButton.setImageDrawable(ContextCompat.getDrawable(this@MenuDetailActivity,R.drawable.bitmap_grey))
            }
            else if(binding?.model?.cartData!=null){
                binding.clShadowButton.tvButtonLabel.text="Update cart (${getString(R.string.price,price?.roundToLong().toString())})"
                binding.clShadowButton.ivButton.setImageDrawable(ContextCompat.getDrawable(this@MenuDetailActivity,R.drawable.bitmap_orange))
            }
            else if(allPossibleCombination(false)){
                binding.clShadowButton.tvButtonLabel.text="Add to cart (${getString(R.string.price,price?.roundToLong().toString())})"
                binding.clShadowButton.ivButton.setImageDrawable(ContextCompat.getDrawable(this@MenuDetailActivity,R.drawable.bitmap_orange))
            }

            else {
                binding.clShadowButton.tvButtonLabel.text="Add to cart (${getString(R.string.price,price?.roundToLong().toString())})"
                binding.clShadowButton.ivButton.setImageDrawable(ContextCompat.getDrawable(this@MenuDetailActivity,R.drawable.bitmap_light_orange))
            }
        }catch (e: Exception){
            Log.e("message",e.message?:"")
        }
    }

    private fun checkPreviousValue(key : Int?): Boolean {
        var ret=false
        if(singleChoiceMap.isNotEmpty()) ret=singleChoiceMap.containsKey(key)
        return ret
    }

    override fun onBackPressed() {
        if(singleChoiceMap.isNullOrEmpty() && multipleChoiceMap.isNullOrEmpty()) super.onBackPressed()
        else ErrorUtil.snackView(binding.root,getString(R.string.clear_choices))
    }



}