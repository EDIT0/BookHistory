package com.ejstudio.bookhistory.util

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ejstudio.bookhistory.R

object BindingAdapter {

    @BindingAdapter("setImage", "Error")
    @JvmStatic
    fun a(iv: ImageView, url: String?, error: Drawable){
        Glide.with(iv.context)
            .load(url)
            .error(error)
            .into(iv)
    }

    @BindingAdapter("setText")
    @JvmStatic
    fun b(tv: TextView, text: String?){
        if(text.equals("") || text.equals("...")) {
            tv.text = " - "
        } else {
            tv.text = text
        }
    }

    @BindingAdapter("setDateSubString")
    @JvmStatic
    fun dateSubString(tv: TextView, text: String?){
        if(text.equals("")) {
            tv.text = " - "
        } else {
            tv.text = text?.substring(0,4)
        }
    }
}