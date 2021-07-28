package com.saveeat.ui.adapter.cart

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterCartBinding

class CartAdapter (var context: Context?) : RecyclerView.Adapter<CartAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.MyViewHolder = MyViewHolder(AdapterCartBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: CartAdapter.MyViewHolder, position: Int) {
        holder.binding.mp.paintFlags=holder.binding.mp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    override fun getItemCount(): Int =3

    inner class MyViewHolder(var binding: AdapterCartBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            binding.clQuantity.ivPlus.setOnClickListener(this)
            binding.clQuantity.ivMinus.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.ivPlus ->{
                    var count =binding.clQuantity.tvQuantity.text.toString().toInt()
                    count += 1
                    binding.clQuantity.tvQuantity.text=count.toString()
                }

                R.id.ivMinus ->{
                    var count =binding.clQuantity.tvQuantity.text.toString().toInt()
                    if(count>0){
                        count += 1
                        binding.clQuantity.tvQuantity.text=count.toString()
                    }
                }
            }
        }
    }
}