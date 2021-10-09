package com.saveeat.ui.adapter.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterCuisinesBinding
import com.saveeat.databinding.AdapterCuisinesBindingImpl
import com.saveeat.databinding.AdapterFilterCategoryBinding
import com.saveeat.databinding.AdapterRatingBinding
import com.saveeat.model.request.rating.RatingStarModel
import com.saveeat.model.response.saveeat.bean.CuisineBean


class FilterCategoryAdapter(var context: Context?, var list: MutableList<CuisineBean?>?,var listner: (Int?)->Unit) : RecyclerView.Adapter<FilterCategoryAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterCategoryAdapter.MyViewHolder =MyViewHolder(AdapterFilterCategoryBinding.inflate(LayoutInflater.from(context),parent,false))
    override fun onBindViewHolder(holder: FilterCategoryAdapter.MyViewHolder, position: Int) {
        holder.binding.data= list?.get(position) as CuisineBean
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = list?.size?:0

    inner class MyViewHolder(var binding: AdapterFilterCategoryBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
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