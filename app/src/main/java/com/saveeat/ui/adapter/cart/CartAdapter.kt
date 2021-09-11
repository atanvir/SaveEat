package com.saveeat.ui.adapter.cart

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterCartBinding
import com.saveeat.model.response.saveeat.cart.CartDataModel
import com.saveeat.ui.dialog.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class CartAdapter(var context: Context?,var list: MutableList<CartDataModel?>?,var listner: CartItemAdapter.setOnClickListner) : RecyclerView.Adapter<CartAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.MyViewHolder = MyViewHolder(AdapterCartBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: CartAdapter.MyViewHolder, position: Int) {
        holder.binding.data=list?.get(position)
        if(list?.get(position)?.expanded==true) holder.binding.clMain.performClick()
        holder.binding.rvCartItem.layoutManager=LinearLayoutManager(context)
        holder.binding.rvCartItem.adapter=CartItemAdapter(context,position,list?.get(position)?.productData,listner)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int =list?.size?:0

    inner class MyViewHolder(var binding: AdapterCartBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.clMain.setOnClickListener(this)
            binding.btnPickNow.setOnClickListener(this)
            binding.btnPickLater.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
             R.id.btnPickNow ->{
                 list?.get(adapterPosition)?.orderType="Now"
                 list?.get(adapterPosition)?.pickupDate=SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
                 list?.get(adapterPosition)?.pickupTime=SimpleDateFormat("HH:mm").format(Calendar.getInstance().time)
                 binding.btnPickNow.text=list?.get(adapterPosition)?.orderType
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                     binding.btnPickNow.backgroundTintList=context?.getColorStateList(R.color.app_theme)
                     binding.btnPickNow.setTextColor(context?.getColor(R.color.white)!!)
                 }
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                     binding.btnPickLater.backgroundTintList=context?.getColorStateList(R.color.white)
                     binding.btnPickLater.setTextColor(context?.getColor(R.color.app_theme)!!)
                 }

                 binding.btnPickLater.text = context?.getString(R.string.pick_up_later)
             }
             R.id.btnPickLater -> {
                 DatePickerFragment(binding,list?.get(adapterPosition)).show((context as AppCompatActivity).supportFragmentManager,"")

             }

              R.id.clMain -> {
                 if(binding.rvCartItem.visibility== View.VISIBLE) {
                     list?.get(adapterPosition)?.expanded=false
                     binding.tvRestroName.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(context!!,R.drawable.fi_sr_angle_small_left),null)
                     binding.btnPickLater.visibility=View.GONE
                     binding.btnPickNow.visibility=View.GONE
                     binding.rvCartItem.visibility=View.GONE
                 }
                 else {
                     list?.get(adapterPosition)?.expanded=true
                     binding.tvRestroName.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(context!!,R.drawable.fi_sr_angle_small_left_2),null)
                     binding.rvCartItem.visibility=View.VISIBLE
                     binding.btnPickNow.visibility=View.VISIBLE
                     binding.btnPickLater.visibility=View.VISIBLE
                 }
              }
            }
        }
    }
}
