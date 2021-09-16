package com.saveeat.ui.adapter.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterRestaurantHomeBinding
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.main.home.RestaurantProductModel
import com.saveeat.ui.activity.main.MainActivity
import com.saveeat.ui.activity.menu.menu.MenuActivity
import com.saveeat.ui.adapter.restaurant.RestaurantProductHomeAdapter
import com.saveeat.utils.application.KeyConstants

class RestaurantHomeAdapter(var context : Context?, var list : MutableList<RestaurantResponseBean?>?, var type : String?,var onFavClick : ((Int,String)->Unit),var cloneList: MutableList<RestaurantResponseBean?>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),Filterable {

    override fun getItemCount(): Int = list?.size?:0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
       return RestroViewHolder(AdapterRestaurantHomeBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RestroViewHolder -> {
                if(type?.equals("Favourite",ignoreCase = true)==true){
                    list?.get(position)?.isFav=true
                    list?.get(position)?.safetyBadge=list?.get(position)?.restroData?.safetyBadge?:false
                    list?.get(position)?.businessName=list?.get(position)?.restroData?.businessName?:""
                    list?.get(position)?.avgRating=list?.get(position)?.restroData?.avgRating?:0.0
                    list?.get(position)?.logo=list?.get(position)?.restroData?.logo?:""
                    list?.get(position)?.dist=list?.get(position)?.restroData?.dist
                }
                holder.binding.type=type
                holder.binding.model=list?.get(position)
                holder.binding.rvProducts.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                holder.binding.rvProducts.adapter = RestaurantProductHomeAdapter(context,list?.get(position)?.realProductData,cloneList?.get(position)?.realProductData)
                holder.binding.rvProducts.addOnScrollListener(object: RecyclerView.OnScrollListener(){
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if(holder.binding.rvProducts.adapter!=null){
                            val layoutParams= holder.binding.rvProducts.layoutParams as ViewGroup.MarginLayoutParams
                            if((recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()==0 && (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()==0) layoutParams.leftMargin=21
                            else layoutParams.leftMargin=0
                            holder.binding.rvProducts.layoutParams=layoutParams
                            holder.binding.rvProducts.requestLayout()
                        }
                    }

                })
                holder.binding.executePendingBindings()
            }
        }
    }



        inner class RestroViewHolder(var binding : AdapterRestaurantHomeBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.ivViewAll.setOnClickListener(this)
            binding.ivFav.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.ivViewAll->{
                    val intent=Intent(context,MenuActivity::class.java)
                    intent.putExtra("_id",list?.get(adapterPosition)?.menuData?._id)
                    intent.putExtra("type",KeyConstants.RESTAURANT)
                    context?.startActivity(intent)
                }

                R.id.ivFav ->{
                    onFavClick.invoke(adapterPosition,type?:"")
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
                        }else {
                            for(j in cloneList?.get(i)?.realProductData?.indices!!){
                                if(cloneList?.get(i)?.realProductData?.get(j)?.foodName?.contains(constraint.toString(),ignoreCase = true)==true){

                                    var realProductData: MutableList<RestaurantProductModel?>?= ArrayList()
                                    realProductData?.add(cloneList?.get(i)?.realProductData?.get(j))
                                    var cloneNewList=cloneList?.get(i)
                                    cloneNewList?.realProductData=realProductData
                                    filterList?.add(cloneNewList)

                                }
                            }
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