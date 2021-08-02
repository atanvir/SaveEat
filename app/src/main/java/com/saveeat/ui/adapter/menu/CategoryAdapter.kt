package com.saveeat.ui.adapter.menu

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterBrandSectionBinding
import com.saveeat.databinding.AdapterRestaurantSectionBinding
import com.saveeat.databinding.AdapterSaveSectionBinding
import com.saveeat.model.request.brand.BrandModel
import com.saveeat.model.request.restaurant.CategoryModel
import com.saveeat.model.request.restaurant.SaveRestaurantsModel
import com.saveeat.ui.activity.main.MainActivity
import com.saveeat.ui.activity.menu.MenuActivity
import com.saveeat.ui.adapter.brand.BrandAdapter
import com.saveeat.ui.adapter.restaurant.RestaurantAdapter
import com.saveeat.ui.adapter.restaurant.SavedRestaurantAdapter

class CategoryAdapter(var context : Context?, var list : MutableList<Any?>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var BRAND =0
    private var SAVE =1
    private var RESTRO =2

    override fun getItemViewType(position: Int): Int {
        when {
            (list?.get(position) as  CategoryModel).brand?.isNotEmpty()?:false-> { return BRAND }
            (list?.get(position) as  CategoryModel).save?.isNotEmpty()?:false -> { return SAVE }
            else -> { return RESTRO }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        if(viewType==RESTRO) return RestroHolder(AdapterRestaurantSectionBinding.inflate(LayoutInflater.from(context) ,parent,false))
        else if(viewType==BRAND) return BrandViewHolder(AdapterBrandSectionBinding.inflate(LayoutInflater.from(context),parent,false))
        else return SaveViewHolder(AdapterSaveSectionBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is RestroHolder){
            holder.binding.data=list?.get(position) as CategoryModel
            holder.binding.rvProducts.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            holder.binding.rvProducts.adapter= RestaurantAdapter(context,(list?.get(position) as CategoryModel).products,false)
            holder.binding.executePendingBindings()
        }else if(holder is BrandViewHolder){
            holder.binding.saveRestro=list?.get(position) as CategoryModel
            holder.binding.rvBrand.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            holder.binding.rvBrand.adapter= BrandAdapter(context,(list?.get(position) as CategoryModel).brand)
            holder.binding.executePendingBindings()
        }else if(holder is SaveViewHolder){
            holder.binding.restro=list?.get(position) as CategoryModel
            holder.binding.rvProducts.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            holder.binding.rvProducts.adapter= SavedRestaurantAdapter(context,(list?.get(position) as CategoryModel).save)
            holder.binding.executePendingBindings()

        }

    }
    override fun getItemCount(): Int = list?.size?:0

    inner class RestroHolder(var binding : AdapterRestaurantSectionBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            binding.ivViewAll.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.ivViewAll->{
                    context?.startActivity(Intent(context,MenuActivity::class.java))
                }
            }
        }

    }

    inner class BrandViewHolder(var binding : AdapterBrandSectionBinding) : RecyclerView.ViewHolder(binding.root){
        init {

        }

    }

    inner class SaveViewHolder(var binding: AdapterSaveSectionBinding) : RecyclerView.ViewHolder(binding.root){
        init {

        }
    }




}