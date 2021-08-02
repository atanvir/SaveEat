package com.saveeat.ui.adapter.cart

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterCartBinding
import com.saveeat.ui.dialog.CreditDialog
import com.saveeat.ui.dialog.RequirementDialog

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
            binding.tvAddRequirement.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.tvAddRequirement->{
                    val dialog = RequirementDialog()
                    dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_NoTitle);
                    dialog.show((context as AppCompatActivity).supportFragmentManager, "")
                }

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