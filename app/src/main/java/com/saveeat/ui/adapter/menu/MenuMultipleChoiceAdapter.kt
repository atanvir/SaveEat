package com.saveeat.ui.adapter.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterMenuMultipleChoiceBinding
import com.saveeat.model.response.saveeat.menu.MenuExtraModel

class MenuMultipleChoiceAdapter(var context: Context?,var list: MutableList<MenuExtraModel?>?,var listner: (Int,Boolean)-> Unit) : RecyclerView.Adapter<MenuMultipleChoiceAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuMultipleChoiceAdapter.MyViewHolder =MyViewHolder(AdapterMenuMultipleChoiceBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: MenuMultipleChoiceAdapter.MyViewHolder, position: Int) {
        holder.binding.data=list?.get(position)
        if(position== list?.size!!-1) holder.binding.viewLine.visibility=View.GONE
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = list?.size?:0

    inner class MyViewHolder(var binding : AdapterMenuMultipleChoiceBinding): RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            binding.rbChoiceName.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.rbChoiceName -> {

                    listner.invoke(adapterPosition,binding.rbChoiceName.isChecked)
                }
            }
        }
    }
}