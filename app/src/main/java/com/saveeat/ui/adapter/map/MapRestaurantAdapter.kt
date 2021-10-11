package com.saveeat.ui.adapter.map

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterRestaurantMapBinding
import com.saveeat.model.request.address.PlacesModel
import com.saveeat.model.response.saveeat.bean.MenuDataModel
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.main.home.RestaurantProductModel
import com.saveeat.ui.activity.menu.detail.MenuDetailActivity
import com.saveeat.ui.activity.menu.menu.MenuActivity
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.GoogleUtils
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.KeyConstants.BRAND

class MapRestaurantAdapter(var context: Context?, var list: MutableList<RestaurantResponseBean?>?,var cloneList: MutableList<RestaurantResponseBean?>?) : RecyclerView.Adapter<MapRestaurantAdapter.MyViewHolder>(),Filterable {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapRestaurantAdapter.MyViewHolder =MyViewHolder(AdapterRestaurantMapBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: MapRestaurantAdapter.MyViewHolder, position: Int) {
        holder.binding.data=list?.get(position)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = list?.size?:0

    inner class MyViewHolder(var binding: AdapterRestaurantMapBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvMain.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.cvMain ->{
                    if(list?.get(adapterPosition)?.storeStatusOne==true && list?.get(adapterPosition)?.storeStatusTwo==true){
                        val intent=Intent(context,MenuActivity::class.java)
                        if(list?.get(adapterPosition)?.userType?.equals(BRAND,ignoreCase = true)==true)intent.putExtra("_id",list?.get(adapterPosition)?._id)
                        else intent.putExtra("_id",list?.get(adapterPosition)?.menuData?._id)
                        intent.putExtra("type",list?.get(adapterPosition)?.userType)
                        context?.startActivity(intent)
                    }else{
                        ErrorUtil.snackView(binding.root,"Sorry! Restaurant is closed")
                    }
                }
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                var filterListResult : MutableList<RestaurantResponseBean?> = ArrayList()
                if (constraint?.isNotEmpty()==true) {
                    for(i in cloneList?.indices!!){
                        if(cloneList?.get(i)?.businessName?.contains(constraint,ignoreCase = true)==true){
                            filterListResult?.add(cloneList?.get(i))
                        }
                    }

                    if(filterListResult?.size>0){
                        filterResults.values=filterListResult
                        filterResults.count=filterListResult?.size}
                } else {
                        filterResults.values=cloneList
                        filterResults.count=cloneList?.size?:0
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
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