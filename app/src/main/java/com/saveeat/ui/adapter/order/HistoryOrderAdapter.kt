package com.saveeat.ui.adapter.order

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.databinding.ItemHistoryTabBinding
import com.saveeat.databinding.ItemUpcomingTabBinding

class HistoryOrderAdapter(var context: Context) :
    RecyclerView.Adapter<HistoryOrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryOrderAdapter.ViewHolder = ViewHolder(
        ItemHistoryTabBinding.inflate(LayoutInflater.from(context), parent, false)
    )

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: HistoryOrderAdapter.ViewHolder, position: Int) {

    }

    inner class ViewHolder(var binding: ItemHistoryTabBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }
    }

}