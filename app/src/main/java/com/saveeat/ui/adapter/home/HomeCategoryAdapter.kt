package com.saveeat.ui.adapter.home

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
import com.saveeat.model.request.restaurant.CategoryModel
import com.saveeat.ui.activity.menu.MenuActivity
import com.saveeat.ui.adapter.brand.BrandAdapter
import com.saveeat.ui.adapter.restaurant.RestaurantAdapter
import com.saveeat.ui.adapter.restaurant.SavedRestaurantAdapter

class HomeCategoryAdapter(var context : Context?, var list : MutableList<Any?>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val BRAND =0
    val SAVE =1
    val RESTRO =2

    override fun getItemViewType(position: Int): Int {
        return when {
            (list?.get(position) as  CategoryModel).brand?.isNotEmpty()?:false-> { BRAND }
            (list?.get(position) as  CategoryModel).save?.isNotEmpty()?:false -> { SAVE }
            else -> { RESTRO }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        return when (viewType) {
            RESTRO -> RestroHolder(AdapterRestaurantSectionBinding.inflate(LayoutInflater.from(context) ,parent,false))
            BRAND -> BrandViewHolder(AdapterBrandSectionBinding.inflate(LayoutInflater.from(context),parent,false))
            else -> SaveViewHolder(AdapterSaveSectionBinding.inflate(LayoutInflater.from(context),parent,false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RestroHolder -> {
                if(position==5) holder.binding.btnViewAll.visibility=View.VISIBLE
                else holder.binding.btnViewAll.visibility=View.GONE

                holder.binding.data=list?.get(position) as CategoryModel
                holder.binding.rvProducts.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                holder.binding.rvProducts.adapter= RestaurantAdapter(context,(list?.get(position) as CategoryModel).products,false)
                holder.binding.executePendingBindings()

            }
            is BrandViewHolder -> {
                holder.binding.saveRestro=list?.get(position) as CategoryModel
                holder.binding.rvBrand.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                holder.binding.rvBrand.adapter= BrandAdapter(context,(list?.get(position) as CategoryModel).brand)
                holder.binding.executePendingBindings()
            }
            is SaveViewHolder -> {
                holder.binding.restro=list?.get(position) as CategoryModel
                holder.binding.rvProducts.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                holder.binding.rvProducts.adapter= SavedRestaurantAdapter(context,(list?.get(position) as CategoryModel).save)
                holder.binding.executePendingBindings()
            }
        }

    }
    override fun getItemCount(): Int = list?.size?:0

    inner class RestroHolder(var binding : AdapterRestaurantSectionBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init { binding.ivViewAll.setOnClickListener(this) }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.ivViewAll->{ context?.startActivity(Intent(context,MenuActivity::class.java)) }
            }
        }

    }

    inner class BrandViewHolder(var binding : AdapterBrandSectionBinding) : RecyclerView.ViewHolder(binding.root){
        init { }
    }

    inner class SaveViewHolder(var binding: AdapterSaveSectionBinding) : RecyclerView.ViewHolder(binding.root){
        init { }
    }




}