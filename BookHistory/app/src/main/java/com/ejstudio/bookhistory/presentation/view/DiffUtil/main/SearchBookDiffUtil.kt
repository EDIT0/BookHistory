package com.ejstudio.bookhistory.presentation.view.DiffUtil.main

import androidx.recyclerview.widget.DiffUtil
import com.ejstudio.bookhistory.domain.model.SearchBookModel

class SearchBookDiffUtil(
    private val oldTiles: List<SearchBookModel.Document>,
    private val newTiles: List<SearchBookModel.Document>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTiles[oldItemPosition].isbn == newTiles[newItemPosition].isbn
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