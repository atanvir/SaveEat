package com.saveeat.ui.adapter.restaurant

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterRestaurantBinding
import com.saveeat.model.request.restaurant.RestaurantProductModel
import com.saveeat.ui.activity.menu.MenuDetailActivity


class RestaurantAdapter(var context: Context?, var list: MutableList<RestaurantProductModel?>?,var menu: Boolean?) : RecyclerView.Adapter<RestaurantAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(AdapterRestaurantBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.mp.paintFlags= holder.binding.mp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.data=list?.get(position)
        holder.binding.executePendingBindings()
    }
    override fun getItemCount(): Int = list?.size?:0

    inner class MyViewHolder(var binding : AdapterRestaurantBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            if(menu==true){
                val vp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 550)
                binding.cvMain.layoutParams = vp
            }

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