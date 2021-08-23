package com.saveeat.ui.adapter.map

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterRestaurantMapBinding
import com.saveeat.model.response.saveeat.bean.MenuDataModel
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.main.home.RestaurantProductModel
import com.saveeat.ui.activity.menu.detail.MenuDetailActivity
import com.saveeat.ui.activity.menu.menu.MenuActivity
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.KeyConstants.BRAND

class MapRestaurantAdapter(var context: Context?, var list:  MutableList<RestaurantResponseBean?>?) : RecyclerView.Adapter<MapRestaurantAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapRestaurantAdapter.MyViewHolder =MyViewHolder(AdapterRestaurantMapBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: MapRestaurantAdapter.MyViewHolder, position: Int) {
        holder.binding.data=list?.get(position)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = list?.size?:0

    inner class MyViewHolder(var binding: AdapterRestaurantMapBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvMain.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.cvMain ->{
                    val intent=Intent(context,MenuActivity::class.java)
                    if(list?.get(adapterPosition)?.userType?.equals(BRAND,ignoreCase = true)==true)intent.putExtra("_id",list?.get(adapterPosition)?._id)
                    else intent.putExtra("_id",list?.get(adapterPosition)?.menuData?._id)
                    intent.putExtra("type",list?.get(adapterPosition)?.userType)
                    context?.startActivity(intent)
                }
            }
        }
    }
}