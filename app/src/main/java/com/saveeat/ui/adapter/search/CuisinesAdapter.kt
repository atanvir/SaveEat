package com.saveeat.ui.adapter.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.databinding.AdapterCuisinesBinding
import com.saveeat.databinding.AdapterRecentSearchBinding
import com.saveeat.model.response.saveeat.bean.CuisineBean
import com.saveeat.model.response.saveeat.main.search.SearchBean

class CuisinesAdapter(var context : Context,var list: MutableList<SearchBean?>?) : RecyclerView.Adapter<CuisinesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuisinesAdapter.MyViewHolder = MyViewHolder(AdapterCuisinesBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: CuisinesAdapter.MyViewHolder, position: Int) {
        holder.binding.data=list?.get(position)
        holder.binding.executePendingBindings()

    }

    override fun getItemCount(): Int = list?.size?:0

    class MyViewHolder(var binding: AdapterCuisinesBinding): RecyclerView.ViewHolder(binding.root){
        init { }
    }
}