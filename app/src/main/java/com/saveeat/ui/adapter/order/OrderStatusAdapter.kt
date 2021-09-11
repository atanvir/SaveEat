package com.saveeat.ui.adapter.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.databinding.AdapterOrderStatusBinding
import com.saveeat.model.request.order.OrderStatusModel

class OrderStatusAdapter(var context: Context?,var list: List<OrderStatusModel?>?): RecyclerView.Adapter<OrderStatusAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderStatusAdapter.MyViewHolder = MyViewHolder(AdapterOrderStatusBinding.inflate(LayoutInflater.from(context),parent,false))
    override fun onBindViewHolder(holder: OrderStatusAdapter.MyViewHolder, position: Int) {
        holder.binding.data=list?.get(position)
        if(position==(list?.size!!-1)) holder.binding.viewLine.visibility= View.GONE

        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = list?.size?:0
    inner class MyViewHolder(var binding : AdapterOrderStatusBinding) : RecyclerView.ViewHolder(binding.root)
}