package com.saveeat.ui.adapter.restaurant

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterSaveRestaurantBinding
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.ui.activity.menu.detail.MenuDetailActivity
import com.saveeat.utils.application.KeyConstants

class SavedRestaurantAdapter(var context: Context?, var list: MutableList<RestaurantResponseBean?>) : RecyclerView.Adapter<SavedRestaurantAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(AdapterSaveRestaurantBinding.inflate(LayoutInflater.from(context),parent,false))
    override fun getItemCount(): Int = list?.size?:0
    override fun onBindViewHolder(holder: SavedRestaurantAdapter.MyViewHolder, position: Int) {
        holder.binding.mp.paintFlags= holder.binding.mp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.model=list?.get(position)
        holder.binding.executePendingBindings()
    }

    inner class MyViewHolder(var binding : AdapterSaveRestaurantBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvMain.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
                when(v?.id){
                    R.id.cvMain->{
                        context?.startActivity(Intent(context, MenuDetailActivity::class.java).putExtra("_id",list?.get(adapterPosition)?._id).putExtra("type",KeyConstants.RESTAURANT))
                    }
                }
        }

    }
}