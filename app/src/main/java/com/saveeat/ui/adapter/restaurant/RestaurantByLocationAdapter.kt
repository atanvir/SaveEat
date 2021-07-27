package com.saveeat.ui.adapter.restaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.databinding.AdapterRestaurantByLocationBinding
import com.saveeat.model.request.restaurant.RestaurantByLocationModel

class RestaurantByLocationAdapter(var context: Context?,var list: MutableList<RestaurantByLocationModel?>?) : RecyclerView.Adapter<RestaurantByLocationAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantByLocationAdapter.MyViewHolder  = MyViewHolder(AdapterRestaurantByLocationBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: RestaurantByLocationAdapter.MyViewHolder, position: Int) {
        holder.binding.data=list?.get(position)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int =list?.size?:0

    inner class MyViewHolder(var binding: AdapterRestaurantByLocationBinding) : RecyclerView.ViewHolder(binding.root){
        init {

        }
    }
}