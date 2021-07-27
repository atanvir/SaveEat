package com.saveeat.utils.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

class TextViewBindings {

    companion object{
        @BindingAdapter(value = ["setDrawableLeftTextView"])
        @JvmStatic
        fun setFromResource(textView: TextView?, drawable: Int?){
            if(drawable!=null) textView?.setCompoundDrawablesWithIntrinsicBounds(textView.context.getDrawable(drawable?:0),null,null,null)
            else textView?.visibility= View.GONE
        }

    }
}