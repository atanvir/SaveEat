package com.saveeat.ui.adapter.order

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.Until
import com.saveeat.R
import com.saveeat.databinding.AdapterOrderHistoryBinding
import com.saveeat.model.response.saveeat.order.OrderBean
import com.saveeat.model.response.saveeat.order.OrderData
import kotlin.math.roundToInt

class HistoryOrderAdapter(var context: Context,var list: MutableList<OrderBean?>?,var listner: (Int) -> Unit) : RecyclerView.Adapter<HistoryOrderAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryOrderAdapter.MyViewHolder = MyViewHolder(AdapterOrderHistoryBinding.inflate(LayoutInflater.from(context), parent, false))
    override fun getItemCount(): Int = list?.size?:0

    override fun onBindViewHolder(holder: HistoryOrderAdapter.MyViewHolder, position: Int) {
        holder.binding.model=list?.get(position)
        holder.binding.tvSaveLabel.visibility=View.GONE

        if(position==0) holder.binding.cvMain.performClick()

        holder.binding.rvOrder.layoutManager=LinearLayoutManager(context)
        holder.binding.rvOrder.adapter=PastOrderAdapter(context,list?.get(position)?.orderData)

        val wordtoSpan: Spannable = SpannableString("You saved ${context.getString(R.string.price,""+ (list?.get(position)?.saveAmount ?: 0.0).roundToInt())} on this order")
        wordtoSpan.setSpan(ForegroundColorSpan(Color.rgb(0, 178, 17)), 9, wordtoSpan.length-13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.binding.tvSaveLabelOutSide.text = wordtoSpan

        holder.binding.executePendingBindings()
    }

    inner class MyViewHolder(var binding: AdapterOrderHistoryBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvMain.setOnClickListener(this)
            binding.taxInfo.setOnClickListener(this)
            binding.clTaxInfo.ivCLose.setOnClickListener(this)
            binding.ivStartOne.setOnClickListener(this)
            binding.ivStarTwo.setOnClickListener(this)
            binding.ivStarThree.setOnClickListener(this)
            binding.ivStarFour.setOnClickListener(this)
            binding.ivStarFive.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){

                R.id.taxInfo ->{ binding.clTaxInfo.clTaxInfo.visibility=View.VISIBLE }
                R.id.ivCLose ->{ binding.clTaxInfo.clTaxInfo.visibility=View.GONE }

                R.id.cvMain->{
                    if(binding.clOrderDetail.visibility==View.VISIBLE) {
                        binding.clOrderDetail.visibility=View.GONE
                        binding.tvProductName.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(context,R.drawable.fi_sr_angle_small_left),null)
                    }else{
                        binding.clOrderDetail.visibility=View.VISIBLE
                        binding.tvProductName.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(context,R.drawable.fi_sr_angle_small_left_2),null)
                    }
                }



                R.id.ivStartOne ->{
                    listner?.invoke(adapterPosition)
                }

                R.id.ivStarTwo ->{
                    binding.ivStartOne.performClick()
                }

                R.id.ivStarThree ->{
                    binding.ivStartOne.performClick()
                }

                R.id.ivStarFour ->{
                    binding.ivStartOne.performClick()
                }


                R.id.ivStarFive ->{
                    binding.ivStartOne.performClick()
                }

            }
        }
    }

}