package com.saveeat.ui.adapter.brand

import android.content.Context
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterBrandBinding
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.ui.activity.menu.menu.MenuActivity
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants

class BrandAdapter(var context : Context?,var list: MutableList<RestaurantResponseBean?>?,var cloneList : MutableList<RestaurantResponseBean?>?) : RecyclerView.Adapter<BrandAdapter.MyViewHolder>() ,Filterable{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandAdapter.MyViewHolder=  MyViewHolder(AdapterBrandBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: BrandAdapter.MyViewHolder, position: Int) {
        holder.binding.data=list?.get(position)
        if(list?.get(position)?.storeStatusOne!=true || list?.get(position)?.storeStatusTwo!=true){
            holder.binding.ivLogo.colorFilter= ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f)})
        }

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
                    if(list?.get(adapterPosition)?.storeStatusOne!=true || list?.get(adapterPosition)?.storeStatusTwo!=true){
                        ErrorUtil.snackView(binding.root,"Sorry! Restaurant is closed")
                    }else{
                        val intent=Intent(context,MenuActivity::class.java)
                        intent.putExtra("_id",list?.get(adapterPosition)?._id)
                        intent.putExtra("type",KeyConstants.BRAND)
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
                    val filterList: MutableList<RestaurantResponseBean?> = ArrayList()

                    for(i in cloneList?.indices!!){
                        if(cloneList?.get(i)?.businessName?.contains(constraint.toString(),ignoreCase = true)==true){
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
        }    }
}