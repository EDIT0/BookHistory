package com.ejstudio.bookhistory.presentation.view.DiffUtil.main

import androidx.recyclerview.widget.DiffUtil
import com.ejstudio.bookhistory.domain.model.RecentPopularBookModel

class RecentPopularBookDiffUtil(
    private val oldTiles: List<RecentPopularBookModel.Response.Doc>,
    private val newTiles: List<RecentPopularBookModel.Response.Doc>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTiles[oldItemPosition].doc.isbn13 == newTiles[newItemPosition].doc.isbn13
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