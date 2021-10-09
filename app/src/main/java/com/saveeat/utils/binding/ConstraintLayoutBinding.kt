package com.saveeat.utils.binding

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.saveeat.R

class ConstraintLayoutBinding {
    companion object {
        @BindingAdapter(value = ["isCheck"], requireAll = true)
        @JvmStatic
        fun setBackground(view: ConstraintLayout?, isCheck: Boolean) {
            if(isCheck) view?.setBackgroundResource(R.drawable.drawable_rating_check)
            else view?.setBackgroundResource(R.drawable.drawable_rating_round)
        }
    }
}