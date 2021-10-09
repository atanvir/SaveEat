package com.saveeat.ui.adapter.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterRatingBinding
import com.saveeat.model.request.rating.RatingStarModel


class FilterRatingAdapter(var context: Context?, var list: MutableList<RatingStarModel?>?,var listner:(Int) ->Unit) : RecyclerView.Adapter<FilterRatingAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterRatingAdapter.MyViewHolder =MyViewHolder(AdapterRatingBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: FilterRatingAdapter.MyViewHolder, position: Int) {
       holder.binding.data=list?.get(position)
       holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = list?.size?:0

    inner class MyViewHolder(var binding: AdapterRatingBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.clMain.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.clMain ->{ listner.invoke(adapterPosition) }
            }
        }

    }
}