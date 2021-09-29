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
import com.saveeat.databinding.AdapterCartItemBinding
import com.saveeat.model.response.saveeat.cart.ProductDataModel
import com.saveeat.ui.dialog.order.RequirementDialog
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.extn.snack

class CartItemAdapter(var context: Context?,var parentPostion: Int?,var list : List<ProductDataModel?>?,var listner: setOnClickListner)  : RecyclerView.Adapter<CartItemAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemAdapter.MyViewHolder = MyViewHolder(AdapterCartItemBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: CartItemAdapter.MyViewHolder, position: Int) {
        holder.binding.mp.paintFlags=holder.binding.mp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.data=list?.get(position)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int =list?.size?:0

    inner class MyViewHolder(var binding: AdapterCartItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.clQuantity.ivPlus.setOnClickListener(this)
            binding.clQuantity.ivMinus.setOnClickListener(this)
            binding.tvAddRequirement.setOnClickListener(this)
            binding.ivRemove.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){

                R.id.tvAddRequirement-> {
                    val dialog = RequirementDialog(list?.get(adapterPosition),listner,parentPostion,adapterPosition)
                    dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_NoTitle);
                    dialog.show((context as AppCompatActivity).supportFragmentManager, "")
                }

                R.id.ivRemove -> { listner?.removeCart(parentPostion,adapterPosition) }

                R.id.ivPlus ->{
                    if(list?.get(adapterPosition)?.productDetail?.sellingStatus==false) listner.updateCart(parentPostion,adapterPosition,+1)
                    else if(list?.get(adapterPosition)?.productDetail?.leftQuantity?:0>list?.get(adapterPosition)?.quantity?:0) listner.updateCart(parentPostion,adapterPosition,+1)
                    else ErrorUtil.snackView(binding.root,"No more quantity left for today")
                }
                R.id.ivMinus ->{ listner.updateCart(parentPostion,adapterPosition,-1) }
            }
        }
    }

    interface setOnClickListner {
        fun updateCart(parentPostion : Int?,childPostion: Int?,count : Int?)
        fun removeCart(parentPostion : Int?,childPostion: Int?)
    }
}