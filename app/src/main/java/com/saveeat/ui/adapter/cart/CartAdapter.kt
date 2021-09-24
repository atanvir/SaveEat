package com.saveeat.ui.adapter.cart

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterCartBinding
import com.saveeat.model.response.saveeat.cart.CartDataModel
import com.saveeat.model.response.saveeat.cart.ProductDataModel
import com.saveeat.model.response.saveeat.menu.TodayDataModel
import com.saveeat.ui.dialog.picker.DatePickerFragment
import com.saveeat.utils.application.CommonUtils
import com.saveeat.utils.application.ErrorUtil
import java.text.SimpleDateFormat
import java.util.*

class CartAdapter(var context: Context?,var list: MutableList<CartDataModel?>?,var listner: CartItemAdapter.setOnClickListner) : RecyclerView.Adapter<CartAdapter.MyViewHolder>() {
    private var dateTimeFormat=SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US)
    private var weekDateFormat=SimpleDateFormat("EEEE", Locale.US)

    var currentDate=Calendar.getInstance().time
    val serverFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val sdf = SimpleDateFormat(serverFormat, Locale.US)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.MyViewHolder = MyViewHolder(AdapterCartBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: CartAdapter.MyViewHolder, position: Int) {
        dateTimeFormat.timeZone = TimeZone.getTimeZone("GMT+05:30")
        weekDateFormat.timeZone = TimeZone.getTimeZone("GMT+05:30")
        sdf.timeZone = TimeZone.getTimeZone("GMT+05:30")


        holder.binding.data=list?.get(position)
        if(list?.get(position)?.expanded==true) holder.binding.clMain.performClick()
        holder.binding.rvCartItem.layoutManager=LinearLayoutManager(context)
        holder.binding.btnPickNow.performClick()


        for(i in list?.get(position)?.productData?.indices!!){
            var maxDate=CommonUtils.calculateMaxDate(list?.get(position)?.productData)
            if(maxDate>0){
            if(currentDate.time> maxDate){
                holder.binding.btnPickLater.visibility=View.GONE
                break
            }
            }

            if(list?.get(position)?.productData?.get(i)?.productDetail?.pickupLaterAllowance==true){
                holder.binding.btnPickLater.visibility=View.VISIBLE
                break
            }

        }

        holder.binding.rvCartItem.adapter=CartItemAdapter(context,position,list?.get(position)?.productData,listner)
        holder.binding.executePendingBindings()
    }


    private fun selectedTime(todayDataModel: MutableList<TodayDataModel?>?, day: String) : Pair<Date?,Date?>? {

        var pair : Pair<Date?,Date?>?=Pair(Calendar.getInstance().time,Calendar.getInstance().time)

        for(i in todayDataModel?.indices!!)
            if(todayDataModel?.get(i)?.day?.equals(day,ignoreCase = true)==true){
                pair=Pair(dateTimeFormat.parse("${CommonUtils.calculateDate(currentDate)} ${todayDataModel[i]?.pickupWindowOpen}"),dateTimeFormat.parse("${CommonUtils.calculateDate(currentDate)} ${todayDataModel[i]?.pickupWindowClose}"))
                break
            }

        return pair
    }

    override fun getItemCount(): Int =list?.size?:0

    inner class MyViewHolder(var binding: AdapterCartBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.clMain.setOnClickListener(this)
            binding.btnPickNow.setOnClickListener(this)
            binding.btnPickLater.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
             R.id.btnPickNow ->{
                 val now=Calendar.getInstance()
                 now.add(Calendar.MINUTE,15)
                 list?.get(adapterPosition)?.orderType="Now"
                 list?.get(adapterPosition)?.pickupDate=SimpleDateFormat("yyyy-MM-dd").format(now.time)
                 list?.get(adapterPosition)?.pickupTime=SimpleDateFormat("HH:mm").format(now.time)
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                     binding.btnPickNow.backgroundTintList=context?.getColorStateList(R.color.app_theme)
                     binding.btnPickNow.setTextColor(context?.getColor(R.color.white)!!)
                 }
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                     binding.btnPickLater.backgroundTintList=context?.getColorStateList(R.color.white)
                     binding.btnPickLater.setTextColor(context?.getColor(R.color.app_theme)!!)
                 }

                 binding.btnPickLater.text = context?.getString(R.string.pick_up_later)
             }
             R.id.btnPickLater -> {
                 DatePickerFragment(binding,list?.get(adapterPosition)).show((context as AppCompatActivity).supportFragmentManager,"")

             }

              R.id.clMain -> {
                 if(binding.clMainBelow.visibility== View.VISIBLE) {
                     list?.get(adapterPosition)?.expanded=false
                     binding.tvRestroName.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(context!!,R.drawable.fi_sr_angle_small_left),null)
                     binding.clMainBelow.visibility=View.GONE
                 }
                 else {
                     list?.get(adapterPosition)?.expanded=true
                     binding.tvRestroName.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(context!!,R.drawable.fi_sr_angle_small_left_2),null)
                     binding.clMainBelow.visibility=View.VISIBLE
                 }
              }
            }
        }
    }
}
