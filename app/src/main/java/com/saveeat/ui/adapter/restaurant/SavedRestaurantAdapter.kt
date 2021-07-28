package com.saveeat.ui.adapter.restaurant

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterRestaurantBinding
import com.saveeat.databinding.AdapterSaveRestaurantBinding
import com.saveeat.model.request.restaurant.RestaurantProductModel
import com.saveeat.model.request.restaurant.SaveRestaurantsModel
import com.saveeat.ui.activity.menu.MenuDetailActivity

class SavedRestaurantAdapter(var context: Context?, var list: MutableList<SaveRestaurantsModel?>?) : RecyclerView.Adapter<SavedRestaurantAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(AdapterSaveRestaurantBinding.inflate(LayoutInflater.from(context),parent,false))
    override fun getItemCount(): Int = list?.size?:0
    override fun onBindViewHolder(holder: SavedRestaurantAdapter.MyViewHolder, position: Int) {
        holder.binding.mp.paintFlags= holder.binding.mp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.data=list?.get(position)
        holder.binding.executePendingBindings()
    }

    inner class MyViewHolder(var binding : AdapterSaveRestaurantBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            binding.cvMain.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
                when(v?.id){
                    R.id.cvMain->{ context?.startActivity(Intent(context,MenuDetailActivity::class.java)) }
                }
        }

    }
}