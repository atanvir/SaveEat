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
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterOrderHistoryBinding

class HistoryOrderAdapter(var context: Context) : RecyclerView.Adapter<HistoryOrderAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryOrderAdapter.MyViewHolder = MyViewHolder(AdapterOrderHistoryBinding.inflate(LayoutInflater.from(context), parent, false))
    override fun getItemCount(): Int = 6

    override fun onBindViewHolder(holder: HistoryOrderAdapter.MyViewHolder, position: Int) {
        holder.binding.clBilling.tvSaveLabel.visibility=View.GONE
        val wordtoSpan: Spannable = SpannableString("You saved ₹427 on this order")
        wordtoSpan.setSpan(ForegroundColorSpan(Color.GREEN), 9, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.binding.tvSaveLabelOutSide.text = wordtoSpan
    }

    inner class MyViewHolder(var binding: AdapterOrderHistoryBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvMain.setOnClickListener(this)
            binding.clBilling.taxInfo.setOnClickListener(this)
            binding.clBilling.clTaxInfo.ivCLose.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.taxInfo ->{
                    binding.clBilling.clTaxInfo.clTaxInfo.visibility=View.VISIBLE

                }
                R.id.ivCLose ->{
                    binding.clBilling.clTaxInfo.clTaxInfo.visibility=View.GONE
                }

                R.id.cvMain->{
                    if(binding.clOrderDetail.visibility==View.VISIBLE) {
                        binding.clOrderDetail.visibility=View.GONE
                        binding.tvProductName.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(context,R.drawable.fi_sr_angle_small_left),null)


                    }else{
                        binding.clOrderDetail.visibility=View.VISIBLE
                        binding.tvProductName.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(context,R.drawable.fi_sr_angle_small_left_2),null)

                    }
                }
            }
        }
    }

}