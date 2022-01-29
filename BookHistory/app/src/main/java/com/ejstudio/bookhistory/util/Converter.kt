package com.ejstudio.bookhistory.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

import android.util.DisplayMetrics




object Converter {

    fun pxToDp(context: Context, px: Int): Int {
        val resources: Resources = context.getResources()
        val metrics: DisplayMetrics = resources.getDisplayMetrics()
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT).toInt()
    }

    fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.getResources().getDisplayMetrics()).toInt()
    }

}