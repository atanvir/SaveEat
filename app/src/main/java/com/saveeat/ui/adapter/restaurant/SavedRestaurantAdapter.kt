package com.saveeat.ui.adapter.restaurant

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterSaveRestaurantBinding
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.ui.activity.menu.detail.MenuDetailActivity
import com.saveeat.utils.application.KeyConstants
import java.util.*
import kotlin.collections.ArrayList

class SavedRestaurantAdapter(var context: Context?, var list: MutableList<RestaurantResponseBean?>,var cloneList :  MutableList<RestaurantResponseBean?>) : RecyclerView.Adapter<SavedRestaurantAdapter.MyViewHolder>(), Filterable {
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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filterResults = FilterResults()
                if (constraint.isNotEmpty()) {
                    val filterList: MutableList<RestaurantResponseBean?> = ArrayList()

                    for(i in cloneList?.indices!!){
                        if(cloneList?.get(i)?.restroData?.businessName?.contains(constraint.toString(),ignoreCase = true)==true || cloneList?.get(i)?.foodName?.contains(constraint.toString(),ignoreCase = true)==true){
                            filterList?.add(cloneList?.get(i))
                        }
                    }
                    filterResults.values=filterList
                    filterResults.count= filterList?.size?:0

                }else{
                    filterResults.values=cloneList
                    filterResults.count=cloneList?.size?:0
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                list?.clear()
                if (results?.count>0) {
                    list?.addAll(results.values as Collection<RestaurantResponseBean?>)
                    notifyDataSetChanged()
                }else{
                    notifyDataSetChanged()
                }
            }
        }
    }
}