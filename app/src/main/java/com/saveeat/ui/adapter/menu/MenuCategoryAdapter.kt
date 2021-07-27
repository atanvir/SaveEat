package com.saveeat.ui.adapter.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterMenuCategoryBinding
import com.saveeat.model.request.menu.MenuCategoryModel

class MenuCategoryAdapter(var context: Context?,var list: List<MenuCategoryModel?>?) : RecyclerView.Adapter<MenuCategoryAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuCategoryAdapter.MyViewHolder = MyViewHolder(AdapterMenuCategoryBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: MenuCategoryAdapter.MyViewHolder, position: Int) {
        holder.binding.data=list?.get(position)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = list?.size?:0

    inner class MyViewHolder(var binding: AdapterMenuCategoryBinding): RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            binding.clMain.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.clMain ->{
                    for(i in list?.indices!!){
                        list?.get(i)?.selected=false
                    }

                    list?.get(adapterPosition)?.selected=true
                    notifyDataSetChanged()
                }
            }
        }
    }

}