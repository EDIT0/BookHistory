package com.ejstudio.bookhistory.data.repository.main.booklist.local

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.BookListEntity

interface BookListLocalDataSource {
    fun getBeforeReadBookList(email: String, reading_state: String) : LiveData<List<BookListEntity>>
    fun getReadingBookList(email: String, reading_state: String) : LiveData<List<BookListEntity>>
    fun getEndReadBookList(email: String, reading_state: String) : LiveData<List<BookListEntity>>
}