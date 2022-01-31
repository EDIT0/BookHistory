package com.ejstudio.bookhistory.util

import android.content.Context
import android.graphics.Rect
import android.view.View

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class OffsetItemDecoration(context: Context, start: Int, middle: Int, end: Int) : ItemDecoration() {
    var context: Context
    private val mStart: Int
    private val mMiddle: Int
    private val mEnd: Int

    init {
        this.context = context
        mStart = start
        mMiddle = middle
        mEnd = end
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = Converter.dpToPx(context, mStart)
        } else if (parent.getChildAdapterPosition(view) == 9) {
            outRect.left = Converter.dpToPx(context, mMiddle)
            outRect.right = Converter.dpToPx(context,mEnd)
        } else {
            outRect.left = Converter.dpToPx(context, mMiddle)
        }
    }


}