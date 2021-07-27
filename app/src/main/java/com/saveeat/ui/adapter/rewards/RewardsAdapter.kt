package com.saveeat.ui.adapter.rewards

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterRewardsBinding
import com.saveeat.model.request.reward.RewardModel

class RewardsAdapter(var context: Context?,var list : List<RewardModel?>?) : RecyclerView.Adapter<RewardsAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardsAdapter.MyViewHolder = MyViewHolder(AdapterRewardsBinding.inflate(LayoutInflater.from(context),parent,false))
    override fun onBindViewHolder(holder: RewardsAdapter.MyViewHolder, position: Int) {
        holder.binding.data=list?.get(position)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int =list?.size?:0

    inner class MyViewHolder(var binding: AdapterRewardsBinding): RecyclerView.ViewHolder(binding.root){

    }
}