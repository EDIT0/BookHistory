package com.ejstudio.bookhistory.data.repository.main.booklist.local

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.db.BookListDao
import com.ejstudio.bookhistory.data.model.BookListEntity

class BookListLocalDataSourcelmpl(
    private val bookListDao: BookListDao
) : BookListLocalDataSource {
    override fun getBeforeReadBookList(email: String, reading_state: String): LiveData<List<BookListEntity>> {
        return bookListDao.getBookList(email, reading_state)
    }

    override fun getReadingBookList(email: String, reading_state: String): LiveData<List<BookListEntity>> {
        return bookListDao.getBookList(email, reading_state)
    }

    override fun getEndReadBookList(email: String, reading_state: String): LiveData<List<BookListEntity>> {
        return bookListDao.getBookList(email, reading_state)
    }
}