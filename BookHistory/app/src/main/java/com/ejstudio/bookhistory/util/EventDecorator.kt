package com.ejstudio.bookhistory.util

import android.content.Context
import android.graphics.drawable.Drawable
import com.ejstudio.bookhistory.R
import com.prolificinteractive.materialcalendarview.spans.DotSpan

import com.prolificinteractive.materialcalendarview.DayViewFacade

import com.prolificinteractive.materialcalendarview.CalendarDay

import com.prolificinteractive.materialcalendarview.DayViewDecorator


class EventDecorator(
    private val context: Context,
    private val color: Int,
    dates: Collection<CalendarDay>
) : DayViewDecorator {

    private var dates: HashSet<CalendarDay>? = null
    lateinit var drawable: Drawable

    init {
        this.dates = HashSet(dates)
        drawable = context.getDrawable(R.drawable.event_decorator_circle)!!
    }

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates!!.contains(day!!)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(5F, color))
        view.setSelectionDrawable(drawable)

    }

}