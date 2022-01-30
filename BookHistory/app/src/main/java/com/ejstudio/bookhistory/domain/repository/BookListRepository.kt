package com.ejstudio.bookhistory.domain.repository

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.BookListEntity

interface BookListRepository {
    fun getBeforeReadBookList(email: String, reading_state: String) : LiveData<List<BookListEntity>>
    fun getReadingBookList(email: String, reading_state: String) : LiveData<List<BookListEntity>>
    fun getEndReadBookList(email: String, reading_state: String) : LiveData<List<BookListEntity>>
}