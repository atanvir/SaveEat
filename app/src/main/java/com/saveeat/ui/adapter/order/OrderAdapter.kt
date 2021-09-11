package com.saveeat.ui.adapter.order

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.databinding.AdapterOrderBinding
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.order.OrderData

class OrderAdapter(var context: Context?, var list: MutableList<OrderData?>?, var restroData: RestaurantResponseBean?) : RecyclerView.Adapter<OrderAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAdapter.MyViewHolder = MyViewHolder(AdapterOrderBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: OrderAdapter.MyViewHolder, position: Int) {
        holder.binding.mp.paintFlags=holder.binding.mp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.model=list?.get(position)
        holder.binding.restroData=restroData
        holder.binding.executePendingBindings()
    }
    override fun getItemCount(): Int = list?.size?:0

    inner class MyViewHolder(var binding: AdapterOrderBinding): RecyclerView.ViewHolder(binding.root)  {
        init {

        }

    }

}