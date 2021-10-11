package com.saveeat.ui.adapter.restaurant

import android.content.Context
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterPopularRestaurantSearchBinding
import com.saveeat.databinding.AdapterRestaurantByLocationBinding
import com.saveeat.model.request.filter.FilterRequestModel
import com.saveeat.model.request.restaurant.RestaurantByLocationModel
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.main.home.RestaurantProductModel
import com.saveeat.ui.activity.menu.menu.MenuActivity
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants

class RestaurantByLocationAdapter(var context: Context?,var list : MutableList<RestaurantResponseBean?>?,var cloneList : MutableList<RestaurantResponseBean?>?,var listner : (Int)-> Unit,var type: String?) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  =
        if(viewType==0) SearchViewHolder(AdapterPopularRestaurantSearchBinding.inflate(LayoutInflater.from(context),parent,false))
        else RestaurantViewHolder(AdapterRestaurantByLocationBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder){
            is RestaurantViewHolder ->{
                holder.binding.data=list?.get(position)
                if(list?.get(position)?.storeStatusOne!=true || list?.get(position)?.storeStatusTwo!=true) holder.binding.imageView2.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f)})
                holder.binding.executePendingBindings()
            }
            is SearchViewHolder ->{
                holder.binding.data=list?.get(position)
                if(list?.get(position)?.storeStatusOne!=true || list?.get(position)?.storeStatusTwo!=true) holder.binding.imageView2.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f)})
                holder.binding.executePendingBindings()
            }
        }

    }

    override fun getItemViewType(position: Int): Int = if(type.equals("Search")) 0 else 1


    override fun getItemCount(): Int =list?.size?:0

    inner class RestaurantViewHolder(var binding: AdapterRestaurantByLocationBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvMain.setOnClickListener(this)
            binding.ivFav.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.cvMain->{
                    if(type?.equals("Filter")==true){
                        if(list?.get(adapterPosition)?.storeStatusOne==true && list?.get(adapterPosition)?.storeStatusTwo==true)
                            context?.startActivity(Intent(context, MenuActivity::class.java)
                                                   .putExtra("_id",list?.get(adapterPosition)?.menuData?._id)
                                                   .putExtra("type",KeyConstants.RESTAURANT)
                                                   .putExtra("filter",(context as AppCompatActivity).intent?.getParcelableExtra<FilterRequestModel?>("filter")))

                        else ErrorUtil.snackView(binding.root,"Sorry! Restaurant is closed")

                    }else{
                    if(list?.get(adapterPosition)?.storeStatusOne==true && list?.get(adapterPosition)?.storeStatusTwo==true) context?.startActivity(Intent(context, MenuActivity::class.java).putExtra("_id",list?.get(adapterPosition)?.menuData?._id).putExtra("type",KeyConstants.RESTAURANT))
                    else ErrorUtil.snackView(binding.root,"Sorry! Restaurant is closed")
                    }

                }
                R.id.ivFav ->{ listner.invoke(adapterPosition) }
            }
        }
    }

    inner class SearchViewHolder(var binding: AdapterPopularRestaurantSearchBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvMain.setOnClickListener(this)
            binding.ivFav.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.cvMain->{
                    if(list?.get(adapterPosition)?.storeStatusOne==true && list?.get(adapterPosition)?.storeStatusTwo==true) context?.startActivity(Intent(context, MenuActivity::class.java).putExtra("_id",list?.get(adapterPosition)?.menuData?._id).putExtra("type",KeyConstants.RESTAURANT))
                    else ErrorUtil.snackView(binding.root,"Sorry! Restaurant is closed")

                }
                R.id.ivFav ->{ listner.invoke(adapterPosition) }
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val filterList: MutableList<RestaurantResponseBean?> = ArrayList()

                if (constraint?.isNotEmpty()==true) {

                    for(i in cloneList?.indices!!){
                        if(cloneList?.get(i)?.businessName?.contains(constraint.toString(),ignoreCase = true)==true){
                            filterList?.add(cloneList?.get(i))
                        }
                    }


                }
                if(filterList?.size>0){
                    filterResults.values=filterList
                    filterResults.count= filterList?.size?:0
                }else{
                    filterResults.values=cloneList
                    filterResults.count= cloneList?.size?:0
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
    }    }

