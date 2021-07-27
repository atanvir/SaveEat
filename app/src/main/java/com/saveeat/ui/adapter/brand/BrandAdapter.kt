package com.saveeat.ui.adapter.brand

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.databinding.AdapterBrandBinding
import com.saveeat.model.request.brand.BrandModel

class BrandAdapter (var context : Context?,var list: MutableList<BrandModel?>?) : RecyclerView.Adapter<BrandAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandAdapter.MyViewHolder=  MyViewHolder(AdapterBrandBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: BrandAdapter.MyViewHolder, position: Int) {
        holder.binding.data=list?.get(position)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = list?.size?:0

    inner class MyViewHolder(var binding : AdapterBrandBinding) : RecyclerView.ViewHolder(binding.root){
        init {

        }
    }
}