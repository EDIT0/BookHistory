package com.ejstudio.bookhistory.data.repository.main.booklist.local

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.BookListEntity
import io.reactivex.rxjava3.core.Completable

interface BookListLocalDataSource {
    fun getTotalBookList(email: String) : LiveData<List<BookListEntity>>
    fun getBeforeReadBookList(email: String, reading_state: String) : LiveData<List<BookListEntity>>
    fun getReadingBookList(email: String, reading_state: String) : LiveData<List<BookListEntity>>
    fun getEndReadBookList(email: String, reading_state: String) : LiveData<List<BookListEntity>>
    fun getIdxBookInfo(email: String, idx: Int, reading_state: String) : LiveData<BookListEntity>
    fun deleteIdxBookInfo(email: String, idx: Int) : Completable
}