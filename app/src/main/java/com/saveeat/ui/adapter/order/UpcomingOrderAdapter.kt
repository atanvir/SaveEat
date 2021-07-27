package com.saveeat.ui.adapter.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.databinding.AdapterRestaurantBinding
import com.saveeat.databinding.ItemUpcomingTabBinding

class UpcomingOrderAdapter(var context: Context?): RecyclerView.Adapter<UpcomingOrderAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingOrderAdapter.MyViewHolder= MyViewHolder(ItemUpcomingTabBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun getItemCount(): Int= 5

    override fun onBindViewHolder(holder: UpcomingOrderAdapter.MyViewHolder, position: Int) {

    }

    inner class MyViewHolder(var binding: ItemUpcomingTabBinding): RecyclerView.ViewHolder(binding.root){
        init {

        }
    }

}