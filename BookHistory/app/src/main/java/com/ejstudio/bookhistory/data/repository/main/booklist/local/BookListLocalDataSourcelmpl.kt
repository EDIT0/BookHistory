package com.ejstudio.bookhistory.data.repository.main.booklist.local

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.db.BookListDao
import com.ejstudio.bookhistory.data.model.BookListEntity
import io.reactivex.rxjava3.core.Completable

class BookListLocalDataSourcelmpl(
    private val bookListDao: BookListDao
) : BookListLocalDataSource {

    override fun getTotalBookList(email: String): LiveData<List<BookListEntity>> {
        return bookListDao.getAllBookList(email)
    }

    override fun getBeforeReadBookList(email: String, reading_state: String): LiveData<List<BookListEntity>> {
        return bookListDao.getBookList(email, reading_state)
    }

    override fun getReadingBookList(email: String, reading_state: String): LiveData<List<BookListEntity>> {
        return bookListDao.getBookList(email, reading_state)
    }

    override fun getEndReadBookList(email: String, reading_state: String): LiveData<List<BookListEntity>> {
        return bookListDao.getBookList(email, reading_state)
    }

    override fun getIdxBookInfo(email: String, idx: Int, reading_state: String): LiveData<BookListEntity> {
        return bookListDao.getIdxBookInfo(email, idx, reading_state)
    }

    override fun deleteIdxBookInfo(email: String, idx: Int): Completable {
        return bookListDao.deleteIdxBookInfo(email, idx)
    }

    override fun updateBookReadingState(email: String, idx: Int, reading_state: String): Completable {
        return bookListDao.updateBookReadingState(email, idx, reading_state)
    }
}