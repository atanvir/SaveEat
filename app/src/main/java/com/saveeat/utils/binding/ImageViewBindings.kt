package com.saveeat.utils.binding

import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.saveeat.utils.extn.loadNormalPhoto
import com.saveeat.utils.extn.loadProfilePic

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


        @BindingAdapter(value = ["loadGIFFromResource"])
        @JvmStatic
        fun loadGIFFromResource(imageView: ImageView?, drawable: Int?){
            Glide.with(imageView?.context!!).asGif().load(drawable).into(imageView)
//            imageView?.setImageResource(drawable!!)
        }


        @BindingAdapter(value = ["imageUrl", "progressbar"], requireAll = true)
        @JvmStatic
        fun loadProfilePic(view: ImageView?, imageUrl: String?, progressBar: ProgressBar?) {
            view?.loadProfilePic(imageUrl, progressBar)
        }


        @BindingAdapter(value = ["normalPhotoUrl", "progressbar"], requireAll = true)
        @JvmStatic
        fun loadNormalPhoto(view: ImageView?, imageUrl: String?, progressBar: ProgressBar?) {
            view?.loadNormalPhoto(imageUrl, progressBar)
        }






    }
}