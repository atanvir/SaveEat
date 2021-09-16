package com.saveeat.ui.adapter.menu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterMenuMultipleChoiceBinding
import com.saveeat.databinding.AdapterMenuSingleChoiceBinding
import com.saveeat.model.response.saveeat.menu.MenuCategoryModel
import com.saveeat.model.response.saveeat.menu.MenuExtraModel
import com.saveeat.ui.dialog.ErrorDialog
import com.saveeat.utils.application.CommonUtils

class MenuChoiceAdapter(var context: Context?, var data: MenuCategoryModel?,  var listner :MenuCustomizationListner?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val SINGLE_CHOICES=2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if(viewType==SINGLE_CHOICES) SingleChoiceViewHolder(AdapterMenuSingleChoiceBinding.inflate(LayoutInflater.from(context),parent,false))
        else MultipleChoiceViewHolder(AdapterMenuMultipleChoiceBinding.inflate(LayoutInflater.from(context),parent,false))


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if(position==(data?.choice?.size?:1)-1) data?.choice?.get(position)?.last=true
        if(holder is SingleChoiceViewHolder){
            holder.binding.data=data?.choice?.get(position)
            holder.binding.executePendingBindings()
        }else if(holder is MultipleChoiceViewHolder){
            holder.binding.data=data?.choice?.get(position)
            holder.binding.executePendingBindings()
        }

    }

    override fun getItemCount(): Int = data?.choice?.size?:0

    inner class SingleChoiceViewHolder(var binding : AdapterMenuSingleChoiceBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.rbChoiceName.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.rbChoiceName -> {
                    for(i in data?.choice?.indices!!){
                        if(i!=adapterPosition) data?.choice?.get(i)?.check=false
                    }
                    data?.choice?.get(adapterPosition)?.check=!(data?.choice?.get(adapterPosition)?.check!!)
                    listner?.onMenuSingleItemClick(adapterPosition,data)
                    notifyDataSetChanged()
                }
            }
        }
    }


    inner class MultipleChoiceViewHolder(var binding : AdapterMenuMultipleChoiceBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.rbChoiceName.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.rbChoiceName -> {
                    if(((data?.count?:0)+1) >data?.max?:0) {
                        if(data?.choice?.get(adapterPosition)?.check==true)  {
                            data?.count = (data?.count?:0)-1
                            data?.choice?.get(adapterPosition)?.check=false
                            listner?.onMenuMultipleItemClick(adapterPosition,data)
                        }
                        else {
                            CommonUtils.showDialog(context!!,"You can select "+data?.categoryName+" "+ CommonUtils.calculateCombinationValidation(data?.min,data?.max))
                            data?.choice?.get(adapterPosition)?.check=false
                            notifyItemChanged(adapterPosition)
                        }

                    }
                    else {
                        data?.choice?.get(adapterPosition)?.check = !(data?.choice?.get(adapterPosition)?.check!!)
                        if(data?.choice?.get(adapterPosition)?.check==true) data?.count =(data?.count?:0)+1
                        else data?.count =(data?.count?:0)-1
                        listner?.onMenuMultipleItemClick(adapterPosition,data)
                    }

                }
            }
        }


    }
}