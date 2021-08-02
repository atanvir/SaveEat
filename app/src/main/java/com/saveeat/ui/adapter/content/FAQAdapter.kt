package com.saveeat.ui.adapter.content

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.databinding.AdapterFaqBinding

class FAQAdapter(var context: Context) : RecyclerView.Adapter<FAQAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQAdapter.MyViewHolder = MyViewHolder(AdapterFaqBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: FAQAdapter.MyViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = 5
    inner class MyViewHolder(var binding: AdapterFaqBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.mainCV.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.mainCV ->{
                    if(binding.clSubDes.visibility==View.GONE){
                        binding.headerText.typeface= ResourcesCompat.getFont(context!!, R.font.inter_semi_bold)
                        binding.clSubDes.visibility=View.VISIBLE
                        binding.headerText.setTextColor(ContextCompat.getColor(context,R.color.app_theme))
                        binding.headerText.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(context,R.drawable.fi_sr_angle_small_left_2),null)
                    }else{
                        binding.headerText.typeface= ResourcesCompat.getFont(context!!, R.font.inter_regular)
                        binding.clSubDes.visibility=View.GONE
                        binding.headerText.setTextColor(ContextCompat.getColor(context,R.color.black))
                        binding.headerText.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(context,R.drawable.fi_sr_angle_small_left),null)

                    }
                }
            }
        }
    }
}