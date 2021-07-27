package com.saveeat.ui.adapter.autocomplete

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.biteMe.ui.adapter.autocomplete.onAutoCompleteItemClick
import com.saveeat.R
import com.saveeat.databinding.AdapterAutoCompleteAddressBinding
import com.saveeat.model.request.address.PlacesModel
import com.saveeat.utils.application.GoogleUtils.autocomplete


class AutoCompleteAddressAdapter(var context: Context, var list: MutableList<PlacesModel?>?, var listner: onAutoCompleteItemClick?) : RecyclerView.Adapter<AutoCompleteAddressAdapter.MyViewHolder>(), Filterable {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(AdapterAutoCompleteAddressBinding.inflate(LayoutInflater.from(context),parent,false))
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.data=list?.get(position)
        holder.binding.executePendingBindings()
    }
    override fun getItemCount(): Int = list?.size?:0

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filterResults = FilterResults()
                if (constraint.isNotEmpty()) {
                   val resultList: List<PlacesModel?>? = autocomplete(constraint.toString())
                   filterResults.values=resultList
                   filterResults.count= resultList?.size?:0
                }else{
                    val resultList: List<PlacesModel?>?= ArrayList()
                    filterResults.values=resultList
                    filterResults.count=0
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                list?.clear()
                listner?.loader(false)
                if (results?.count>0) {
                    list?.addAll(results.values as Collection<PlacesModel?>)
                    notifyDataSetChanged()
                }else{
                    notifyDataSetChanged()
                }
            }
        }
    }

    inner class MyViewHolder(var binding: AdapterAutoCompleteAddressBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.clMain.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.clMain ->{ listner?.onClick(list?.get(adapterPosition)?.placeId,list?.get(adapterPosition)?.spotName) }
            }
        }
    }
}