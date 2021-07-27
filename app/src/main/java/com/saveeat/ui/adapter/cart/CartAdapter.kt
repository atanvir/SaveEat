package com.saveeat.ui.adapter.cart

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.databinding.AdapterCartBinding

class CartAdapter (var context: Context?) : RecyclerView.Adapter<CartAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.MyViewHolder = MyViewHolder(AdapterCartBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: CartAdapter.MyViewHolder, position: Int) {
        holder.binding.mp.paintFlags=holder.binding.mp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    override fun getItemCount(): Int =3

    inner class MyViewHolder(var binding: AdapterCartBinding) : RecyclerView.ViewHolder(binding.root){
        init {

        }
    }
}