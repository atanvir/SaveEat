package com.saveeat.utils.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.saveeat.utils.extn.loadNormalPhoto

class ImageViewBindings {
    companion object{
        @BindingAdapter(value = ["setFromResource"])
        @JvmStatic
        fun setFromResource(imageView: ImageView?, drawable: Int?){

            imageView?.loadNormalPhoto(drawable,null)
        }



        @BindingAdapter(value = ["setFromResourceDrawable"])
        @JvmStatic
        fun setFromResourceDrawable(imageView: ImageView?, drawable: Int?){

            imageView?.setImageResource(drawable!!)
        }

    }
}