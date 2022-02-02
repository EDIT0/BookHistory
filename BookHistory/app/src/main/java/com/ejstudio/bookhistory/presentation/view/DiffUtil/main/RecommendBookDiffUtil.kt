package com.ejstudio.bookhistory.presentation.view.DiffUtil.main

import androidx.recyclerview.widget.DiffUtil
import com.ejstudio.bookhistory.domain.model.RecommendBookModel

class RecommendBookDiffUtil(
    private val oldTiles: List<RecommendBookModel.Response.Doc>,
    private val newTiles: List<RecommendBookModel.Response.Doc>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTiles[oldItemPosition].book.isbn13 == newTiles[newItemPosition].book.isbn13
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