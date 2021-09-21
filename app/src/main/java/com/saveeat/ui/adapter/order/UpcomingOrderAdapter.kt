package com.saveeat.ui.adapter.order

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.media.ExifInterface
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterOrderUpcomingBinding
import com.saveeat.model.request.order.OrderStatusModel
import com.saveeat.model.response.saveeat.order.OrderBean
import com.saveeat.model.response.saveeat.order.OrderData
import com.saveeat.utils.application.CommonUtils
import com.saveeat.utils.application.RecyclerItemClickListener
import com.saveeat.utils.binding.TextViewBindings.Companion.calculateDate
import com.saveeat.utils.binding.TextViewBindings.Companion.calculateTime
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class UpcomingOrderAdapter(var context: Context?,var list: MutableList<OrderBean?>?,var listner : (Int)-> Unit): RecyclerView.Adapter<UpcomingOrderAdapter.MyViewHolder>(){

    private var countDownTimer : CountDownTimer?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingOrderAdapter.MyViewHolder= MyViewHolder(AdapterOrderUpcomingBinding.inflate(LayoutInflater.from(context),parent,false))
    override fun getItemCount(): Int= list?.size?:0
    override fun onBindViewHolder(holder: UpcomingOrderAdapter.MyViewHolder, position: Int) {
        holder.binding.model=list?.get(position)
        holder.binding.mp.paintFlags=holder.binding.mp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        startTimer(holder.binding,list?.get(position)?.createdAt)


        // Orders
        holder.binding.rvOrders.layoutManager=LinearLayoutManager(context)
        holder.binding.rvOrders.adapter=OrderAdapter(context,/*list?.get(position)?.orderData*/null,list?.get(position)?.restroData)

        // Order Status
         var orderStatus =arrayListOf<OrderStatusModel>(OrderStatusModel(context?.getString(R.string.order_pending),false),
                                                        OrderStatusModel(context?.getString(R.string.order_being_prepared),false),
                                                        OrderStatusModel(context?.getString(R.string.order_ready_to_pickup),false),
                                                        OrderStatusModel(context?.getString(R.string.order_completed),false))

        for(i in orderStatus?.indices!!){
            if(list?.get(position)?.status?.equals("Pending")==true && orderStatus?.get(i)?.name?.equals(context?.getString(R.string.order_pending))==true) {
                orderStatus?.get(i)?.check=true
                break
            }else if(list?.get(position)?.status?.equals("Accepted")==true && orderStatus?.get(i)?.name?.equals(context?.getString(R.string.order_being_prepared))==true) {
                orderStatus?.get(0)?.check=true
                orderStatus?.get(i)?.check=true
                break
            }

            else if(list?.get(position)?.status?.equals("Order Ready for pickup")==true && orderStatus?.get(i)?.name?.equals(context?.getString(R.string.order_ready_to_pickup))==true) {
                orderStatus?.get(0)?.check=true
                orderStatus?.get(1)?.check=true
                orderStatus?.get(i)?.check=true
                break
            }
        }

        holder.binding.rvOrderStatus.layoutManager=LinearLayoutManager(context)
        holder.binding.rvOrderStatus.adapter=OrderStatusAdapter(context,orderStatus)

        holder.binding.rvItemData.layoutManager=LinearLayoutManager(context)
        holder.binding.rvItemData.adapter=PastOrderAdapter(context,/*list?.get(position)?.orderData*/null)

        if(position>0) holder.binding.clMain.visibility=View.GONE
        holder.binding.executePendingBindings()
    }

    private fun startTimer(holder: AdapterOrderUpcomingBinding,date: String?) {
        val myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        if(countDownTimer!=null){
            countDownTimer?.cancel()
        }

        if(date!=null){
            try {
                val date: Date = sdf.parse(date)
                Log.e("server","Date"+""+CommonUtils.calculateDate(date))
                Log.e("server","Time"+""+CommonUtils.calculateTime(date))
                var currentDate=Calendar.getInstance().time

                if(61*1000>(currentDate.time-date.time)){
                if(countDownTimer!=null) countDownTimer=null
                countDownTimer = object : CountDownTimer(61*1000-(currentDate.time-date.time), 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                       if(holder.btnCancelOrder.visibility==View.GONE) holder.btnCancelOrder.visibility=View.VISIBLE
                    }

                    override fun onFinish() {
                        holder.btnCancelOrder.visibility=View.GONE
                    }
                }.start()
                }else{
                    holder.btnCancelOrder.visibility=View.GONE
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


    inner class MyViewHolder(var binding: AdapterOrderUpcomingBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvMain.setOnClickListener(this)
            binding.rlDirection.setOnClickListener(this)
            binding.rlPhoneCall.setOnClickListener(this)
            binding.btnCancelOrder.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){

                R.id.btnCancelOrder ->{
                    listner?.invoke(adapterPosition)
                }

                R.id.cvMain ->{
                    if(binding.clMain.visibility==View.VISIBLE){
                        binding.clMain.visibility=View.GONE
                    }
                    else {
                        binding.clMain.visibility=View.VISIBLE
                    }
                }

                R.id.rlDirection -> {
                    CommonUtils.openGoogleMap(context!!,list?.get(adapterPosition)?.restroData?.latitude,list?.get(adapterPosition)?.restroData?.longitude)
                }

                R.id.rlPhoneCall ->{
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + list?.get(adapterPosition)?.restroData?.mobileNumber))
                    context?.startActivity(intent)
                }
            }
        }

    }



}