package com.saveeat.ui.adapter.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.databinding.AdapterMenuMultipleChoiceBinding

class MenuMultipleChoiceAdapter(var context: Context?) : RecyclerView.Adapter<MenuMultipleChoiceAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuMultipleChoiceAdapter.MyViewHolder =MyViewHolder(AdapterMenuMultipleChoiceBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: MenuMultipleChoiceAdapter.MyViewHolder, position: Int) {
        if(position==9){
            holder.binding.viewLine.visibility=View.GONE
        }
    }

    override fun getItemCount(): Int = 10

    inner class MyViewHolder(var binding : AdapterMenuMultipleChoiceBinding): RecyclerView.ViewHolder(binding.root){
        init {

        }
    }
}