package com.saveeat.ui.adapter.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterSearchSuggestionBinding
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.main.search.SearchBean

class SearchSuggestionAdapter(var context: Context?, var list: MutableList<RestaurantResponseBean?>?, var listner : ()->Unit): RecyclerView.Adapter<SearchSuggestionAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSuggestionAdapter.MyViewHolder = MyViewHolder(AdapterSearchSuggestionBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: SearchSuggestionAdapter.MyViewHolder, position: Int) {
        holder.binding.data=list?.get(position)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = list?.size?:0

    inner class MyViewHolder(var binding: AdapterSearchSuggestionBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.clMain.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.clMain ->{
                    Log.e("clicked","search")
                    listner.invoke()
                }
            }
        }
    }

}