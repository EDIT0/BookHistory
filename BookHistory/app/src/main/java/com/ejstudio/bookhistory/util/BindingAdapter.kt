package com.ejstudio.bookhistory.util

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.data.api.ApiClient

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

    @BindingAdapter("setImageMemo", "Error")
    @JvmStatic
    fun b(iv: ImageView, url: String?, error: Drawable){
        Log.i("BindingAdapter", url.toString())
        Glide.with(iv.context)
            .load(url.toString())
            .error(error)
            .into(iv)
    }
}
//ApiClient.IMAGE_BASE_URL.toString() + url.toString().trim()
//202202070251452089670450.png
//http://edit0@edit0.dothome.co.kr/bookhistory/image/imageMemo/202202070237132089670450.png