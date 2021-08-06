package com.saveeat.ui.adapter.order

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterOrderUpcomingBinding
import com.saveeat.model.request.order.OrderStatusModel

class UpcomingOrderAdapter(var context: Context?): RecyclerView.Adapter<UpcomingOrderAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingOrderAdapter.MyViewHolder= MyViewHolder(AdapterOrderUpcomingBinding.inflate(LayoutInflater.from(context),parent,false))
    override fun getItemCount(): Int= 6
    override fun onBindViewHolder(holder: UpcomingOrderAdapter.MyViewHolder, position: Int) {
        /*if(position>0)*/ holder.binding.clMainBelow.visibility=View.GONE
        holder.binding.mp.paintFlags=holder.binding.mp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.rvOrderStatus.layoutManager=LinearLayoutManager(context)
        holder.binding.rvOrderStatus.adapter=OrderStatusAdapter(context, arrayListOf<OrderStatusModel>(OrderStatusModel(context?.getString(R.string.order_being_prepared),true),
                                                                                                       OrderStatusModel(context?.getString(R.string.order_ready_to_pickup),false),
                                                                                                       OrderStatusModel(context?.getString(R.string.order_completed),false)))
    }

    inner class MyViewHolder(var binding: AdapterOrderUpcomingBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvMain.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.cvMain ->{
                    if(binding.clMainBelow.visibility==View.VISIBLE){
                    binding.clMainBelow.visibility=View.GONE
                    binding.tvRestroName.visibility=View.VISIBLE
                }else{
                    binding.clMainBelow.visibility=View.VISIBLE
                    binding.tvRestroName.visibility=View.GONE
                }
               }
            }
        }
    }

}