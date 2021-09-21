package com.saveeat.ui.adapter.restaurant

import android.content.Context
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterRestaurantByLocationBinding
import com.saveeat.model.request.restaurant.RestaurantByLocationModel
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.ui.activity.menu.menu.MenuActivity
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants

class RestaurantByLocationAdapter(var context: Context?,var list : MutableList<RestaurantResponseBean?>?,var listner : (Int)-> Unit) : RecyclerView.Adapter<RestaurantByLocationAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantByLocationAdapter.MyViewHolder  = MyViewHolder(AdapterRestaurantByLocationBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: RestaurantByLocationAdapter.MyViewHolder, position: Int) {
        holder.binding.data=list?.get(position)
        if(list?.get(position)?.storeStatusOne!=true || list?.get(position)?.storeStatusTwo!=true) holder.binding.imageView2.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f)})
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int =list?.size?:0

    inner class MyViewHolder(var binding: AdapterRestaurantByLocationBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            binding.cvMain.setOnClickListener(this)
            binding.ivFav.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.cvMain->{
                    if(list?.get(adapterPosition)?.storeStatusOne==true && list?.get(adapterPosition)?.storeStatusTwo==true) context?.startActivity(Intent(context, MenuActivity::class.java).putExtra("_id",list?.get(adapterPosition)?.menuData?._id).putExtra("type",KeyConstants.RESTAURANT))
                    else ErrorUtil.snackView(binding.root,"Sorry! Restaurant is closed")

                }
                R.id.ivFav ->{ listner.invoke(adapterPosition) }
            }
        }
    }
}