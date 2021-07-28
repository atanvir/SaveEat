package com.saveeat.ui.adapter.restaurant

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterRestaurantByLocationBinding
import com.saveeat.model.request.restaurant.RestaurantByLocationModel
import com.saveeat.ui.activity.menu.MenuDetailActivity

class RestaurantByLocationAdapter(var context: Context?,var list: MutableList<RestaurantByLocationModel?>?) : RecyclerView.Adapter<RestaurantByLocationAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantByLocationAdapter.MyViewHolder  = MyViewHolder(AdapterRestaurantByLocationBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: RestaurantByLocationAdapter.MyViewHolder, position: Int) {
        holder.binding.data=list?.get(position)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int =list?.size?:0

    inner class MyViewHolder(var binding: AdapterRestaurantByLocationBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            binding.cvMain.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.cvMain->{ context?.startActivity(Intent(context, MenuDetailActivity::class.java)) }
            }
        }
    }
}