package com.saveeat.ui.adapter.menu

import android.content.Context
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterMenuProductBinding
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.main.home.RestaurantProductModel
import com.saveeat.ui.activity.menu.detail.MenuDetailActivity
import android.widget.LinearLayout
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Filter
import android.widget.Filterable
import com.saveeat.model.request.address.PlacesModel
import com.saveeat.utils.application.GoogleUtils
import java.util.HashSet


class MenuProductAdapter(var context: Context?, var list: MutableList<RestaurantResponseBean?>?,var cloneList: MutableList<RestaurantResponseBean?>?,var type : String?) : RecyclerView.Adapter<MenuProductAdapter.MenuRestaurantHolder>(),Filterable {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuProductAdapter.MenuRestaurantHolder = MenuRestaurantHolder(AdapterMenuProductBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: MenuProductAdapter.MenuRestaurantHolder, position: Int) {
        holder.binding.data=list?.get(position)
        holder.binding.type=type
        holder.binding.mp.visibility=View.VISIBLE
        if(type?.equals("Selling")==true || list?.get(position)?.type?.equals("Selling")==true){
            if(list?.get(position)?.type?.equals("Selling")==true) holder.binding.type=list?.get(position)?.type
            holder.binding.mp.paintFlags= holder.binding.mp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            if(list?.get(position)?.leftQuantity==0) holder.binding.ivCoverPhoto.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f)}) }
        else if(type?.equals("Fix")==true || list?.get(position)?.type?.equals("Fix")==true){
            if(list?.get(position)?.type?.equals("Fix")==true) holder.binding.type=list?.get(position)?.type
            if(list?.get(position)?.outOfStock==true)  holder.binding.ivCoverPhoto.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f)})
            val layoutParams : MarginLayoutParams = holder.binding.sp.layoutParams as MarginLayoutParams
            layoutParams.setMargins(0,0,0,0)
            holder.binding.sp.layoutParams=layoutParams
            holder.binding.mp.visibility=View.GONE
        }

        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = list?.size?:0

    inner class MenuRestaurantHolder(var binding : AdapterMenuProductBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvMain.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id) {
                R.id.cvMain-> {
                    val intent= Intent(context, MenuDetailActivity::class.java)
                    intent.putExtra("_id",list?.get(adapterPosition)?._id)
                    context?.startActivity(intent)
                }
            }
        }
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filterList: MutableList<RestaurantResponseBean?> = ArrayList()

                val filterResults = FilterResults()
                if (constraint.isNotEmpty()) {

                    for(i in cloneList?.indices!!){
                        if(cloneList?.get(i)?.foodName?.toLowerCase()?.contains(constraint.toString().toLowerCase(),ignoreCase = true)==true){
                            filterList?.add(cloneList?.get(i))
                        }
                    }

                }
                var set =HashSet<RestaurantResponseBean?>()
                set.addAll(filterList!!)
                filterList.clear()
                filterList.addAll(set)
                filterResults.values=filterList
                filterResults.count= filterList?.size?:0

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