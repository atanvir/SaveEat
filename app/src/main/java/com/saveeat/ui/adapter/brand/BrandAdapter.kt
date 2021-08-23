package com.saveeat.ui.adapter.brand

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterBrandBinding
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.ui.activity.menu.menu.MenuActivity
import com.saveeat.utils.application.KeyConstants

class BrandAdapter (var context : Context?,var list: MutableList<RestaurantResponseBean?>?) : RecyclerView.Adapter<BrandAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandAdapter.MyViewHolder=  MyViewHolder(AdapterBrandBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: BrandAdapter.MyViewHolder, position: Int) {
        holder.binding.data=list?.get(position)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = list?.size?:0

    inner class MyViewHolder(var binding : AdapterBrandBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvMain.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.cvMain ->{
                    val intent=Intent(context,MenuActivity::class.java)
                    intent.putExtra("_id",list?.get(adapterPosition)?._id)
                    intent.putExtra("type",KeyConstants.BRAND)
                    context?.startActivity(intent)
                }
            }
        }
    }
}