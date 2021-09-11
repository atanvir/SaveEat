package com.saveeat.ui.adapter.order

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.databinding.AdapterOrderBinding
import com.saveeat.databinding.AdapterPastOrderBinding
import com.saveeat.model.response.saveeat.order.OrderData

class PastOrderAdapter(var context: Context?,var list: MutableList<OrderData?>?) : RecyclerView.Adapter<PastOrderAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastOrderAdapter.MyViewHolder = MyViewHolder(AdapterPastOrderBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: PastOrderAdapter.MyViewHolder, position: Int) {
        holder.binding.model=list?.get(position)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int=list?.size?:0

    inner class MyViewHolder(var binding: AdapterPastOrderBinding) : RecyclerView.ViewHolder(binding.root){
        init {

        }
    }
}