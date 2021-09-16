package com.saveeat.ui.adapter.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.internal.common.CommonUtils
import com.saveeat.R
import com.saveeat.databinding.AdapterMenuCategoryBinding
import com.saveeat.databinding.AdapterMenuCustomizationBinding
import com.saveeat.model.response.saveeat.menu.MenuCategoryModel
import com.saveeat.model.response.saveeat.menu.MenuExtraModel
import com.saveeat.ui.adapter.menu.MenuCustomizationAdapter.MyViewHolder
import com.saveeat.utils.application.CommonUtils.calculateCombination

class MenuCustomizationAdapter(var context : Context?, var list: MutableList<MenuCategoryModel?>?, var listner :MenuCustomizationListner?) : RecyclerView.Adapter<MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(AdapterMenuCustomizationBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(list?.get(position)?.choice.isNullOrEmpty()) holder.binding.clMain.visibility= View.GONE

        holder.binding.tvCategoryName.text=list?.get(position)?.categoryName
        holder.binding.tvChooseLabel.text= calculateCombination(list?.get(position)?.min,list?.get(position)?.max)

        holder.binding.rvChoices.layoutManager= LinearLayoutManager(context)
        holder.binding.rvChoices.adapter= MenuChoiceAdapter(context,list?.get(position),listner)
    }

    override fun getItemCount(): Int =list?.size?:0


    inner class MyViewHolder(var binding : AdapterMenuCustomizationBinding): RecyclerView.ViewHolder(binding.root) {
    init {

    }
    }
}