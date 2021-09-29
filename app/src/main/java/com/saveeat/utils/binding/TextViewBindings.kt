package com.saveeat.utils.binding

import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.saveeat.R
import com.saveeat.model.request.cart.ChoiceModel
import com.saveeat.model.response.saveeat.cart.ProductDataModel
import com.saveeat.model.response.saveeat.order.OrderBean
import com.saveeat.model.response.saveeat.order.OrderData
import com.saveeat.utils.extn.roundOffDecimal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TextViewBindings {

    companion object{
        @BindingAdapter(value = ["setDrawableLeftTextView"])
        @JvmStatic
        fun setFromResource(textView: TextView?, drawable: Int?){
            if(drawable!=null) textView?.setCompoundDrawablesWithIntrinsicBounds(textView.context.getDrawable(drawable?:0),null,null,null)
            else textView?.visibility= View.GONE
        }


        @BindingAdapter(value = ["choice"])
        @JvmStatic
        fun setChoice(textView: TextView?,list : MutableList<ChoiceModel?>?){
            var newDataList = ArrayList<String>()
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO){
                    for(i in list?.indices!!){
                        newDataList?.add(list?.get(i)?.foodName?:"")
                    }
                }
                withContext(Dispatchers.Main){
                    if(newDataList?.isEmpty()) textView?.visibility=View.GONE
                    else textView?.visibility=View.VISIBLE
                    textView?.text=TextUtils.join(", ",newDataList)
                }
            }

        }


        @BindingAdapter(value = ["calculateDate"])
        @JvmStatic
        fun calculateDate(textView: TextView?,date : String?){
            if(date!=null){
            val myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            try {
                val date: Date = sdf.parse(date)
                textView?.text=SimpleDateFormat("dd-MM-yyyy").format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            }
        }


        @BindingAdapter(value = ["calculateDateWithTime"])
        @JvmStatic
        fun calculateDateWithTime(textView: TextView?,date : String?){
            if(date!=null){
                val myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                sdf.timeZone = TimeZone.getTimeZone("GMT")
                try {
                    val date: Date = sdf.parse(date)
                    textView?.text=SimpleDateFormat("dd MMM yyyy, hh:mm a").format(date)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }



        @BindingAdapter(value = ["calculateTime"])
        @JvmStatic
        fun calculateTime(textView: TextView?,date : String?){
            if(date!=null){
            val myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            try {
                val date: Date = sdf.parse(date)
                textView?.text=SimpleDateFormat("hh:mm a").format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            }
        }

        @BindingAdapter(value = ["choiceWithPrice"])
        @JvmStatic
        fun setChoiceWithPrice(textView: TextView?,data :ProductDataModel?){
            var price: Double?=0.0
            if(data?.type?.equals("Selling")==true) price=data?.productDetail?.offeredPrice?.times(data?.quantity?:0)
            else price=data?.productDetail?.price?.times(data?.quantity?:0)

                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        for(i in data?.choice?.indices!!){
                        price=price?.plus(data.choice?.get(i)?.price?:0.0)
                    }
                    withContext(Dispatchers.Main){
                        textView?.text = textView?.context?.getString(R.string.price,price?.roundOffDecimal().toString())
                    }
            }

        }
        }





        @BindingAdapter(value = ["itemWithChoiceQty"])
        @JvmStatic
        fun setItemWithChoiceQty(textView: TextView?,data :MutableList<OrderData?>?){
            val priceList:  MutableList<String?>?=ArrayList()
            val choiceList : MutableList<String?>?=ArrayList()


            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Main) {
                    for(i in data?.indices!!){

                        for(j in data?.get(i)?.mainChoice?.indices!!){
                            choiceList?.add(data?.get(i)?.mainChoice?.get(j)?.foodName)
                        }

                        var dataNewModel="${data?.get(i)?.quantity} x ${data?.get(i)?.productData?.foodName}"
                        priceList?.add(dataNewModel)

                        }
                    }
                withContext(Dispatchers.Main){
                    if(choiceList?.size?:0>0) {
                        textView?.text = TextUtils.join(",",priceList!!) + " ("+TextUtils.join(",",choiceList!!)+")"
                    }else{
                        textView?.text = TextUtils.join(",",priceList!!)
                    }
                }
            }
        }


        @BindingAdapter(value = ["itemWithChoiceQtyData"])
        @JvmStatic
        fun setItemWithChoiceQtyData(textView: TextView?,data :OrderData?){
            var price: String=""
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    var list : MutableList<String?>?=ArrayList()
                    for(j in data?.mainChoice?.indices!!){
                        list?.add(data?.mainChoice?.get(j)?.foodName)

                    }
                    if(list?.isNotEmpty()==true){
                        price="${data?.quantity} x ${data?.productData?.foodName} (${TextUtils.join(",",list!!)})"
                    }else{
                        price="${data?.quantity} x ${data?.productData?.foodName}"
                    }
                }
                withContext(Dispatchers.Main){
                    textView?.text = price
                }
            }

        }


        @BindingAdapter(value = ["totalItemPrice"])
        @JvmStatic
        fun setTotalItemPrice(textView: TextView?, list: List<ProductDataModel?>?){
            var price: Double=0.0


            if(list?.isNotEmpty()==true){
               CoroutineScope(Dispatchers.Main).launch {
                   withContext(Dispatchers.IO) {
                       for(i in list?.indices!!){
                           for(j in list?.get(i)?.choice?.indices!!){
                               price=price.plus(list?.get(i)?.choice?.get(j)?.price?.times(list?.get(i)?.choice?.get(j)?.quantity?:0)?:0.0)
                           }

                           if(list?.get(i)?.type?.equals("Selling")==true) price=price.plus(list?.get(i)?.productDetail?.offeredPrice?.times(list?.get(i)?.quantity?:0)?:0.0)
                           else price=price.plus(list?.get(i)?.productDetail?.price?.times(list?.get(i)?.quantity?:0)?:0.0)

                       }
                   }
                   withContext(Dispatchers.Main){
                       if(list?.size?:0>1) textView?.setText(""+list?.size+" Items || "+textView?.context?.getString(R.string.price,price?.roundOffDecimal()?.toString()))
                       else textView?.setText(""+list?.size+" Item || "+textView?.context?.getString(R.string.price,price?.roundOffDecimal()?.toString()))
                   }
               }
           }
        }

    }
}