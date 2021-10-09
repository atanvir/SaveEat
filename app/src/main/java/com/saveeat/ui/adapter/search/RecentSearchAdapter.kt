package com.saveeat.ui.adapter.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterRecentSearchBinding
import com.saveeat.model.response.saveeat.main.search.SearchBean
import com.saveeat.model.response.saveeat.main.search.SearchModel

class RecentSearchAdapter(var context : Context,var list: MutableList<SearchBean?>?, var listner : (String?)->Unit) : RecyclerView.Adapter<RecentSearchAdapter.MyViewHolder>() {
    override fun getItemCount(): Int = list?.size?:0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchAdapter.MyViewHolder = MyViewHolder(AdapterRecentSearchBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: RecentSearchAdapter.MyViewHolder, position: Int) {
       holder.binding.data=list?.get(position)
       holder.binding.executePendingBindings()
    }

    inner class MyViewHolder(var binding: AdapterRecentSearchBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.clMain.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.clMain ->{
                    listner?.invoke(list?.get(adapterPosition)?.search)
                }
            }
        }
    }

}