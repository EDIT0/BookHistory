package com.ejstudio.bookhistory.presentation.view.DiffUtil.main

import androidx.recyclerview.widget.DiffUtil
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.domain.model.TextImageMemoModel

class CalendarDateMemoDiffUtil(
    private val oldTiles: List<TextImageMemoModel>,
    private val newTiles: List<TextImageMemoModel>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTiles[oldItemPosition].idx == newTiles[newItemPosition].idx
    }

    override fun getOldListSize(): Int {
        return oldTiles.size
    }

    override fun getNewListSize(): Int {
        return newTiles.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTiles[oldItemPosition] == newTiles[newItemPosition]
    }
}