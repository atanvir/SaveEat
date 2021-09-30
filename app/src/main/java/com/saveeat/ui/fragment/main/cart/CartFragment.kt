package com.saveeat.ui.fragment.main.cart

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.saveeat.R
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.FragmentCartBinding
import com.saveeat.ui.adapter.cart.CartAdapter
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.saveeat.model.request.cart.CartRequestModel
import com.saveeat.model.request.cart.ChoiceModel
import com.saveeat.model.request.cart.RestroSellingModel
import com.saveeat.model.request.cart.UpdateCartModel
import com.saveeat.model.request.order.CartDataPlaceOrderModel
import com.saveeat.model.request.order.OrderPlaceModel
import com.saveeat.model.response.saveeat.cart.CartDataModel
import com.saveeat.model.response.saveeat.cart.DeleteItemCart
import com.saveeat.model.response.saveeat.cart.ProductDataModel
import com.saveeat.model.response.saveeat.cart.TaxCommissionModel
import com.saveeat.razorpay.RazorPay
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.repository.cache.PrefrencesHelper
import com.saveeat.ui.activity.drawer.history.OrderHistoryActivity
import com.saveeat.ui.activity.order.status.OrderStatusActivity
import com.saveeat.ui.adapter.cart.CartItemAdapter
import com.saveeat.utils.application.*
import com.saveeat.utils.application.CommonUtils.buttonLoader
import com.saveeat.utils.application.CommonUtils.getProductType
import com.saveeat.utils.application.CommonUtils.increaseFontSizeForPath
import com.saveeat.utils.application.CustomLoader.Companion.hideLoader
import com.saveeat.utils.extn.roundOffDecimal
import com.saveeat.utils.extn.roundOffDecimalWithTwo
import com.saveeat.utils.extn.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>(), View.OnClickListener, CartItemAdapter.setOnClickListner {
    private var  list: MutableList<CartDataModel?>? =null
    private var mainPosition: Int?=0
    private var childPositionView : Int?=0
    private var quantity: Int=0

    // Billing
    var subTotal : Double?=0.0
    var saveEatFees : Double?=0.0
    var saveTotal : Double?=0.0
    var finalTotal : Double?=0.0
    var taxes : Double?=0.0

    var taxCommissionModel: TaxCommissionModel?=null
    private val viewModel : CartViewModel by viewModels()
    private var paymentGatewayResposne: BroadcastReceiver?=null



    val serverFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val sdf = SimpleDateFormat(serverFormat, Locale.US)
    private var dateTimeFormat=SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)


    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCartBinding = FragmentCartBinding.inflate(inflater,container,false)

    override fun init() {


        sdf.timeZone = TimeZone.getTimeZone("GMT+05:30")
        dateTimeFormat.timeZone = TimeZone.getTimeZone("GMT+05:30")


        startAnimation()
        cartListApi()
    }

    override fun initCtrl() {
        binding.clShadowButton.ivButton.setOnClickListener(this)
        binding.clBilling.taxInfo.setOnClickListener(this)
        binding.clBilling.clTaxInfo.ivCLose.setOnClickListener(this)

    }

    override fun onStart() {
        super.onStart()
        requireActivity().registerReceiver(paymentGatewayResposne, IntentFilter("com.saveeat"))
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(paymentGatewayResposne)
    }

    override fun observer() {
        lifecycleScope.launch {


            paymentGatewayResposne = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    buttonLoader(binding.clShadowButton, true)
                    var paymentId  = intent.getStringExtra("paymentId")
                    viewModel.orderItems(OrderPlaceModel(cartData = getCartData(),
                                                         paymentId = paymentId,
                                                         paymentMode = "RazorPay",
                                                         paymentStatus="Confirm",
                                                         saveAmount=saveTotal,
                                                         subTotal=subTotal,
                                                         tax = saveEatFees?.plus(taxes?:0.0),
                                                         total=finalTotal,
                                                         token= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.jwtToken),
                                                         userId= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants._id),
                                                         saveEatFees=saveEatFees,taxes=taxes))


                }
            }
            viewModel.getCartItem.observe(this@CartFragment,{

                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {

                            CoroutineScope(Dispatchers.Main).launch {

                                withContext(Dispatchers.IO){
                                    list=it.value?.data?.cartData
                                    taxCommissionModel=it?.value?.data?.taxCommission

                                    if(list?.size?:0>0) requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).getOrCreateBadge(R.id.cartFragment).number = list?.size?:0
                                    else requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).removeBadge(R.id.cartFragment)
                                }
                                withContext(Dispatchers.Main){
                                    if(list?.isNullOrEmpty()==false){
                                        list?.get(0)?.expanded=true
                                        binding.clBilling.clBilling.visibility=View.VISIBLE
                                        binding.clShadowButton.clMainShadow.visibility=View.VISIBLE

                                    }else{
                                        binding.clNoData.visibility=View.VISIBLE
                                        binding.clBilling.clBilling.visibility=View.GONE
                                        binding.clShadowButton.clMainShadow.visibility=View.GONE
                                        binding.rvProducts.visibility=View.GONE
                                    }

                                    binding.rvProducts.layoutManager=LinearLayoutManager(requireActivity())
                                    binding.rvProducts.adapter= CartAdapter(requireActivity(),list,this@CartFragment)
                                    calculateBilling()
                                }
                            }


                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) {
                            stopAnimation()
                        }
                    }
                    is Resource.Failure -> {  stopAnimation();
                        ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })

            viewModel.updateCart.observe(this@CartFragment,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) { cartListApi() }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) {  stopAnimation(); ErrorUtil.snackView(binding.root,it.value?.message?:"") }
                    }
                    is Resource.Failure -> {  stopAnimation(); ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })

            viewModel.deleteItemFromCart.observe(this@CartFragment,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0)  { cartListApi() }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) {  stopAnimation(); ErrorUtil.snackView(binding.root,it.value?.message?:"") }
                    }
                    is Resource.Failure -> {  stopAnimation(); ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })


            viewModel.orderItems.observe(this@CartFragment,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0)  {
//                            requireActivity().toast(it.value?.message?:"")
                            requireActivity().startActivity(Intent(requireActivity(),OrderStatusActivity::class.java).putExtra("saveAmount",saveTotal?.toString()))
                            requireActivity().finish()
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) {  hideLoader(); ErrorUtil.snackView(binding.root,it.value?.message?:"") }
                    }
                    is Resource.Failure -> {  hideLoader(); ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })
        }

    }

    private fun cartListApi() {
        viewModel.cartList(CartRequestModel(latitude = PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.latitude),
                                            longitude= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.longitude),
                                            token = PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.jwtToken),
                                            userId= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants._id)))

    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<ConstraintLayout>(R.id.clAddress)?.visibility= View.GONE
        activity?.findViewById<TextView>(R.id.tvTitle)?.text=getString(R.string.cart)
        activity?.findViewById<TextView>(R.id.tvTitle)?.compoundDrawablePadding=16
        activity?.findViewById<TextView>(R.id.tvTitle)?.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null)
        activity?.findViewById<TextView>(R.id.tvTitle)?.visibility= View.VISIBLE
    }

    private fun stopAnimation() {
        binding.clShimmer.shimmerContainer.visibility=View.GONE
        binding.clShimmer.shimmerContainer.stopShimmer()

        if(binding.rvProducts.adapter?.itemCount?:0>0){
            binding.rvProducts.visibility=View.VISIBLE
            binding.clBilling.clBilling.visibility=View.VISIBLE
            binding.clShadowButton.clMainShadow.visibility=View.VISIBLE
        }else{
            binding.clNoData.visibility=View.VISIBLE
        }
    }

    private fun calculateBilling() {
        // Billing
        subTotal=0.0
        saveEatFees=0.0
        saveTotal=0.0
        finalTotal=0.0
        taxes=0.0

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO){
                for(i in list?.indices!!){
                    for(j in list?.get(i)?.productData?.indices!!) {
                        for(k in list?.get(i)?.productData?.get(j)?.choice?.indices!!){
                            subTotal=  subTotal?.plus(list?.get(i)?.productData?.get(j)?.choice?.get(k)?.price?.times(list?.get(i)?.productData?.get(j)?.choice?.get(k)?.quantity?:0)?:0.0)
                        }

                        if(list?.get(i)?.productData?.get(j)?.type?.equals("Selling")==true) subTotal=subTotal?.plus(list?.get(i)?.productData?.get(j)?.productDetail?.offeredPrice?.times(list?.get(i)?.productData?.get(j)?.quantity?:0)?:0.0)
                        else subTotal=subTotal?.plus(list?.get(i)?.productData?.get(j)?.productDetail?.price?.times(list?.get(i)?.productData?.get(j)?.quantity?:0)?:0.0)
                        saveTotal=saveTotal?.plus(list?.get(i)?.productData?.get(j)?.productDetail?.discountAmount?.times(list?.get(i)?.productData?.get(j)?.quantity?:0)?:0.0)
                    }
                }

                taxes=subTotal?.times(taxCommissionModel?.tax?:0.0)?.div(100)
                saveEatFees=taxes?.plus(subTotal?:0.0)?.times(taxCommissionModel?.fee?:0.0)?.div(100)
                finalTotal=subTotal?.plus(saveEatFees?.plus(taxes?:0.0)!!)
            }
            withContext(Dispatchers.Main){
                hideLoader()

                // Billing
                binding.clBilling.tvSubTotal.text=requireActivity().getString(R.string.price,""+subTotal?.roundOffDecimalWithTwo()?.toString())
                binding.clBilling.clTaxInfo.tvSaveEatFees.text=requireActivity().getString(R.string.price,saveEatFees?.roundOffDecimalWithTwo()?.toString())
                binding.clBilling.tvTaxFees.text=requireActivity().getString(R.string.price,saveEatFees?.plus(taxes?:0.0)?.roundOffDecimalWithTwo()?.toString())
                binding.clBilling.tvTotalPrice.text=finalTotal?.roundOffDecimal()?.toString()
                binding.clBilling.clTaxInfo.tvTaxes.text=requireActivity().getString(R.string.price,taxes?.roundOffDecimalWithTwo()?.toString())

                binding.clShadowButton.tvButtonLabel.text=getString(R.string.checkout)
                val wordtoSpan: Spannable = SpannableString("Continue to checkout to save ${requireActivity().getString(R.string.price,""+saveTotal?.roundOffDecimal()?.toString())} on this order")
                wordtoSpan.setSpan(ForegroundColorSpan(Color.rgb(0, 178, 17)), 28, wordtoSpan.length-14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                increaseFontSizeForPath(wordtoSpan, "${requireActivity().getString(R.string.price,""+saveTotal?.roundOffDecimal()?.toString())}", 1.25f)
                binding.clBilling.tvSaveLabel.text = wordtoSpan

                stopAnimation()
            }
        }
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivButton ->{
                if(checkValidation()){
                buttonLoader(binding.clShadowButton, true)
                RazorPay().payload(requireActivity(), Settings.Secure.getString(requireActivity().contentResolver, Settings.Secure.ANDROID_ID),finalTotal?.roundOffDecimal())
                buttonLoader(binding.clShadowButton, false)
                }else{
                    buttonLoader(binding.clShadowButton, false)
            }
            }
            R.id.taxInfo ->{ binding.clBilling.clTaxInfo.clTaxInfo.visibility=View.VISIBLE }
            R.id.ivCLose ->{ binding.clBilling.clTaxInfo.clTaxInfo.visibility=View.GONE }
        }
    }

    private fun checkValidation(): Boolean {
        var ret=true
        var count=0;


        for(i in list?.indices!!){
           var maxTime= CommonUtils.calculateMaxDate(list?.get(i)?.productData)
            if(list?.get(i)?.orderType?.isEmpty()==true  || list?.get(i)?.orderType==null){
                ret=false
                ErrorUtil.snackView(binding.root,"Please select pick up option for "+list?.get(i)?.restroData?.businessName)
                break
            }else if(list?.get(i)?.pickupDate?.isEmpty()==true || list?.get(i)?.pickupDate==null){
                ret=false
                ErrorUtil.snackView(binding.root,"Please select pick up date for "+list?.get(i)?.restroData?.businessName)
                break
            }else if(list?.get(i)?.pickupTime?.isEmpty()==true || list?.get(i)?.pickupTime==null){
                ret=false
                ErrorUtil.snackView(binding.root,"Please select pick up time for "+list?.get(i)?.restroData?.businessName)
                break
            }else if(list?.get(i)?.storeStatusOne!=true || list?.get(i)?.storeStatusTwo!=true){
                ret=false
                ErrorUtil.snackView(binding.root,"Sorry! ${list?.get(i)?.restroData?.businessName} is closed , Please try ordering with another restaurant")
                break
            }else if(maxTime>0 && dateTimeFormat.parse("${list?.get(i)?.pickupDate} ${list?.get(i)?.pickupTime}").time>=maxTime){
                ret=false
                ErrorUtil.snackView(binding.root,"Sorry! ${list?.get(i)?.restroData?.businessName} items has been expired ,Please try with any other item")
                break
            }else{
                if((list?.get(i)?.productData?.any { it?.type=="Selling" }!=true)){
                    ret=false
                    ErrorUtil.snackView(binding.root,"Please select atleast one selling item to the ${list?.get(i)?.restroData?._id}")
                    break
                }
            }
        }


        if(ret && count==list?.size){
            ret=false;
            ErrorUtil.snackView(binding.root,"Please add selling price item")
        }

        return  ret

    }




    private fun getCartData(): MutableList<CartDataPlaceOrderModel?>? {
        var returnList : MutableList<CartDataPlaceOrderModel?>? = ArrayList()
        for(i in list?.indices!!){
            var subTotal=calculateAmount(list?.get(i)?.productData,"price")
            var totalTax=getTax(calculateAmount(list?.get(i)?.productData,"price"),"total")
            Log.e("total",""+subTotal?.plus(totalTax?:0.0))
            val model : CartDataPlaceOrderModel? =CartDataPlaceOrderModel(actualAmount=calculateAmount(list?.get(i)?.productData,"actualAmount"),
                                                                          address= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.address),
                                                                          cartId=list?.get(i)?._id,
                                                                          discountAmount=calculateAmount(list?.get(i)?.productData,"discountAmount"),
                                                                          latitude=PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.latitude).toDouble(),
                                                                          longitude=PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.longitude).toDouble(),
                                                                          orderType=list?.get(i)?.orderType,
                                                                          pickupDate= list?.get(i)?.pickupDate,
                                                                          pickupTime= list?.get(i)?.pickupTime,
                                                                          price=calculateAmount(list?.get(i)?.productData,"price"),
                                                                          restroId=list?.get(i)?.restroId,
                                                                          saveAmount = calculateAmount(list?.get(i)?.productData,"discountAmount"),
                                                                          subTotal=subTotal,
                                                                          saveEatFees=getTax(calculateAmount(list?.get(i)?.productData,"price"),"saveEatFees"),
                                                                          taxes=getTax(calculateAmount(list?.get(i)?.productData,"price"),"taxes"),
                                                                          tax=totalTax,
                                                                          timezone=TimeZone.getDefault().id,
                                                                          total=subTotal?.plus(totalTax?:0.0))
            model?.total=subTotal?.plus(model?.tax!!)
            returnList?.add(model)
        }
        return returnList
    }

    private fun getSaveEatFees(subTotal: Double?): Double? {
        val taxes = subTotal?.times(5)?.div(100)
        val saveEatFees=taxes?.plus(subTotal?:0.0)?.times(2)?.div(100)

        return saveEatFees
    }

    private fun getTax(subTotal: Double?,type: String?): Double? {
        val taxes=subTotal?.times(taxCommissionModel?.tax?:0.0)?.div(100)
        val saveEatFees=taxes?.plus(subTotal?:0.0)?.times(taxCommissionModel?.fee?:0.0)?.div(100)
        if(type?.equals("total")==true) return taxes?.plus(saveEatFees?:0.0)
        else if(type?.equals("taxes")==true) return taxes
        else return saveEatFees
    }

    private fun calculateAmount(list: MutableList<ProductDataModel?>?,type: String): Double? {
        var returnValue: Double?=0.0
        for(i in list?.indices!!){
            when {
                type?.equals("actualAmount") -> returnValue = if(list?.get(i)?.type?.equals("Selling")==true) returnValue?.plus(list?.get(i)?.productDetail?.offeredPrice?.times(list?.get(i)?.quantity!!)!!) else returnValue?.plus(list?.get(i)?.productDetail?.price?.times(list?.get(i)?.quantity!!)!!)

                type?.equals("discountAmount") -> returnValue = if(list?.get(i)?.type?.equals("Selling")==true) returnValue?.plus(list?.get(i)?.productDetail?.discountAmount?.times(list?.get(i)?.quantity!!)!!) else returnValue?.plus(0.0)

                type?.equals("price") -> {
                    var choiceAmount: Double?=0.0
                    for(j in list?.get(i)?.choice?.indices!!){
                        choiceAmount=choiceAmount?.plus(list?.get(i)?.choice?.get(j)?.price?.times(list?.get(i)?.choice?.get(j)?.quantity?:0)!!)
                    }
                    returnValue = if(list?.get(i)?.type?.equals("Selling")==true) returnValue?.plus(list?.get(i)?.productDetail?.offeredPrice?.times(list?.get(i)?.quantity!!)!!) else returnValue?.plus(list?.get(i)?.productDetail?.price?.times(list?.get(i)?.quantity!!)!!)
                    returnValue=returnValue?.plus(choiceAmount?:0.0)
                }
            }
        }

        return returnValue

    }

    override fun updateCart(parentPostion: Int?, childPostion: Int?, count: Int?) {
//        startAnimation()
        mainPosition=parentPostion
        childPositionView=childPostion
        list?.get(parentPostion?:0)?.productData?.get(childPostion?:0)?.quantity=list?.get(parentPostion?:0)?.productData?.get(childPostion?:0)?.quantity?.plus(count?:0)
        quantity=list?.get(parentPostion?:0)?.productData?.get(childPostion?:0)?.quantity?:0
        viewModel?.updateCart(UpdateCartModel(itemId = list?.get(parentPostion?:0)?.productData?.get(childPostion?:0)?.productId,
                                              cartId = list?.get(parentPostion?:0)?._id,
                                              quantity = list?.get(parentPostion?:0)?.productData?.get(childPostion?:0)?.quantity,
                                              choice = getChoiceModel(parentPostion,childPostion,list?.get(parentPostion?:0)?.productData?.get(childPostion?:0)?.quantity!!),
                                              requirement = list?.get(parentPostion?:0)?.productData?.get(childPostion?:0)?.requirement,
                                              token= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.jwtToken),
                                              userId= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants._id),type=getProductType(list?.get(parentPostion?:0)?.productData?.get(childPostion?:0)?.productDetail?.sellingStatus)))
    }

    private fun getChoiceModel(parentPostion: Int?,childPostion: Int?,quantity: Int?): MutableList<ChoiceModel?>? {
        val choiceList: MutableList<ChoiceModel?>? = ArrayList()
        for(i in list?.get(parentPostion?:0)?.productData?.get(childPostion?:0)?.choice?.indices!!){
            list?.get(parentPostion?:0)?.productData?.get(childPostion?:0)?.choice?.get(i)?.quantity=1
        }
        choiceList?.addAll(list?.get(parentPostion?:0)?.productData?.get(childPostion?:0)?.choice?.toList()!!)
        return choiceList
    }

    private fun startAnimation() {
        binding.rvProducts.visibility=View.INVISIBLE
        binding.clBilling.clBilling.visibility=View.INVISIBLE
        binding.clShadowButton.clMainShadow.visibility=View.INVISIBLE
        binding.clNoData.visibility=View.GONE

        binding.clShimmer.shimmerContainer.visibility=View.VISIBLE
        binding.clShimmer.shimmerContainer.startShimmer()

    }

    override fun removeCart(parentPostion: Int?, childPostion: Int?) {
//        startAnimation()
        mainPosition=parentPostion
        childPositionView=childPostion
        try{
            quantity=list?.get(parentPostion?:0)?.productData?.get(childPostion?:0)?.quantity?:0
            viewModel?.deleteItemFromCart(DeleteItemCart(itemId = list?.get(parentPostion?:0)?.productData?.get(childPostion?:0)?.productId,
                                                         cartId = list?.get(parentPostion?:0)?._id,
                                                         token= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.jwtToken),
                                                         userId= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants._id)))

        }catch (e: Exception){


        }
    }





}