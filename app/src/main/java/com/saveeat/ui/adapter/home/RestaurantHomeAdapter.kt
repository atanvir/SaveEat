package com.saveeat.ui.adapter.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterRestaurantHomeBinding
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.ui.activity.main.MainActivity
import com.saveeat.ui.activity.menu.menu.MenuActivity
import com.saveeat.ui.adapter.restaurant.RestaurantProductHomeAdapter
import com.saveeat.utils.application.KeyConstants

    class RestaurantHomeAdapter(var context : Context?, var list : MutableList<RestaurantResponseBean?>?, var type : String?,var onFavClick : ((Int,String)->Unit)) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = list?.size?:0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
       return RestroViewHolder(AdapterRestaurantHomeBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RestroViewHolder -> {
                holder.binding.type=type
                holder.binding.model=list?.get(position)
                holder.binding.rvProducts.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                holder.binding.rvProducts.adapter = RestaurantProductHomeAdapter(context,list?.get(position)?.realProductData)
                holder.binding.executePendingBindings()
            }
        }
    }

    inner class RestroViewHolder(var binding : AdapterRestaurantHomeBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.ivViewAll.setOnClickListener(this)
            binding.ivFav.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.ivViewAll->{
                    val intent=Intent(context,MenuActivity::class.java)
                    intent.putExtra("_id",list?.get(adapterPosition)?.menuData?._id)
                    intent.putExtra("type",KeyConstants.RESTAURANT)
                    context?.startActivity(intent)
                }

                R.id.ivFav ->{
                    onFavClick.invoke(adapterPosition,type?:"")
                }
            }
        }
    }
}