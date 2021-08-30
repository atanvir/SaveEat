package com.saveeat.ui.adapter.menu

import android.content.Context
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterMenuProductBinding
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.main.home.RestaurantProductModel
import com.saveeat.ui.activity.menu.detail.MenuDetailActivity

class MenuProductAdapter(var context: Context?, var list: MutableList<RestaurantResponseBean?>?) : RecyclerView.Adapter<MenuProductAdapter.MenuRestaurantHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuProductAdapter.MenuRestaurantHolder = MenuRestaurantHolder(AdapterMenuProductBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: MenuProductAdapter.MenuRestaurantHolder, position: Int) {
        holder.binding.mp.paintFlags= holder.binding.mp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.data=list?.get(position)
        if(list?.get(position)?.leftQuantity==0) holder.binding.ivCoverPhoto.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f)})
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = list?.size?:0

    inner class MenuRestaurantHolder(var binding : AdapterMenuProductBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvMain.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id) {
                R.id.cvMain-> {
                    val intent= Intent(context, MenuDetailActivity::class.java)
                    intent.putExtra("_id",list?.get(adapterPosition)?._id)
                    context?.startActivity(intent)
                }
            }
        }
    }
}