package com.saveeat.ui.adapter.restaurant

import android.content.Context
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterProductHomeRestaurantBinding
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.main.home.RestaurantProductModel
import com.saveeat.ui.activity.menu.detail.MenuDetailActivity
import com.saveeat.utils.application.ErrorUtil

class RestaurantProductHomeAdapter(var context: Context?, var list: MutableList<RestaurantProductModel?>?,var cloneList: MutableList<RestaurantProductModel?>?,var close : Boolean?) : RecyclerView.Adapter<RestaurantProductHomeAdapter.RestaurantHolder>(),Filterable {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantProductHomeAdapter.RestaurantHolder =  RestaurantHolder(AdapterProductHomeRestaurantBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: RestaurantProductHomeAdapter.RestaurantHolder, position: Int) {
        holder.binding.mp.paintFlags= holder.binding.mp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.data=list?.get(position)
        holder.binding.close=close
        if(close==false) holder.binding.ivCoverPhoto.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f)})
        else if(list?.get(position)?.leftQuantity==0) holder.binding.ivCoverPhoto.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f)})
        holder.binding.executePendingBindings()
    }
    override fun getItemCount(): Int = list?.size?:0

    inner class RestaurantHolder(var binding : AdapterProductHomeRestaurantBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvMain.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id) {
                R.id.cvMain-> {
                    if(close==false){
                        ErrorUtil.snackView(binding.root,"Sorry! restaurant is closed")
                    }else{
                        val intent=Intent(context,MenuDetailActivity::class.java)
                        intent.putExtra("_id",list?.get(adapterPosition)?._id)
                        context?.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filterResults = FilterResults()
                if (constraint.isNotEmpty()) {
                    val filterList: MutableList<RestaurantProductModel?> = ArrayList()

                    for(i in cloneList?.indices!!){
                        if(cloneList?.get(i)?.foodName?.contains(constraint.toString(),ignoreCase = true)==true){
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
                    list?.addAll(results.values as Collection<RestaurantProductModel?>)
                    notifyDataSetChanged()
                }else{
                    notifyDataSetChanged()
                }
            }
        }

    }
}