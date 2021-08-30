package com.saveeat.ui.adapter.distance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterDistanceBinding
import com.saveeat.databinding.AdapterFaqBinding
import com.saveeat.model.request.distance.DistanceModel
import com.saveeat.ui.adapter.content.FAQAdapter

class DistanceAdapter(var context: Context,var list : MutableList<DistanceModel?>?,var listner :(String?)->Unit):  RecyclerView.Adapter<DistanceAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistanceAdapter.MyViewHolder = MyViewHolder(AdapterDistanceBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: DistanceAdapter.MyViewHolder, position: Int) {
        list?.get(position)?.visible = (list?.size?:0) != (position+1)
        holder.binding.data=list?.get(position)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = list?.size?:0
    inner class MyViewHolder(var binding: AdapterDistanceBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.rbChoiceName.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){

                R.id.rbChoiceName ->{
                    for(i in list?.indices!!) list?.get(i)?.check=false
                    list?.get(adapterPosition)?.check=true
                    listner.invoke(list?.get(adapterPosition)?.name)
                    notifyDataSetChanged()
                }
            }
        }
    }
}