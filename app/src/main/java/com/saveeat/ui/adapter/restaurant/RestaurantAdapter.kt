package com.saveeat.ui.adapter.restaurant

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterMenuRestaurantBinding
import com.saveeat.databinding.AdapterRestaurantBinding
import com.saveeat.model.request.restaurant.RestaurantProductModel
import com.saveeat.ui.activity.menu.MenuDetailActivity


class RestaurantAdapter(var context: Context?, var list: MutableList<RestaurantProductModel?>?,var menu: Boolean?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = if(menu==true) MenuRestaurantHolder(AdapterMenuRestaurantBinding.inflate(LayoutInflater.from(context),parent,false)) else RestaurantHolder(AdapterRestaurantBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is RestaurantHolder){
        holder.binding.mp.paintFlags= holder.binding.mp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.data=list?.get(position)
        holder.binding.executePendingBindings()
        }else if(holder is MenuRestaurantHolder){
            holder.binding.mp.paintFlags= holder.binding.mp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.data=list?.get(position)
            holder.binding.executePendingBindings()
        }
    }
    override fun getItemCount(): Int = list?.size?:0

    inner class RestaurantHolder(var binding : AdapterRestaurantBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvMain.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.cvMain->{
                 context?.startActivity(Intent(context,MenuDetailActivity::class.java))
                }
            }
        }

    }


    inner class MenuRestaurantHolder(var binding : AdapterMenuRestaurantBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvMain.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.cvMain->{
                    context?.startActivity(Intent(context,MenuDetailActivity::class.java))
                }
            }
        }

    }
}