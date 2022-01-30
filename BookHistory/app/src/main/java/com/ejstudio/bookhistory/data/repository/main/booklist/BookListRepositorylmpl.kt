package com.ejstudio.bookhistory.data.repository.main.booklist

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.repository.main.booklist.local.BookListLocalDataSource
import com.ejstudio.bookhistory.data.repository.main.booklist.remote.BookListRemoteDataSource
import com.ejstudio.bookhistory.domain.repository.BookListRepository

class BookListRepositorylmpl(
    private val bookListLocalDataSource: BookListLocalDataSource,
    private val bookListRemoteDataSource: BookListRemoteDataSource
) : BookListRepository {

    override fun getBeforeReadBookList(email: String, reading_state: String): LiveData<List<BookListEntity>> {
        return bookListLocalDataSource.getBeforeReadBookList(email, reading_state)
    }

    override fun getReadingBookList(email: String, reading_state: String): LiveData<List<BookListEntity>> {
        return bookListLocalDataSource.getReadingBookList(email, reading_state)
    }

    override fun getEndReadBookList(email: String, reading_state: String): LiveData<List<BookListEntity>> {
        return bookListLocalDataSource.getEndReadBookList(email, reading_state)
    }
}