package com.ejstudio.bookhistory.data.repository.main.booklist

import android.util.Log
import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.repository.main.booklist.local.BookListLocalDataSource
import com.ejstudio.bookhistory.data.repository.main.booklist.remote.BookListRemoteDataSource
import com.ejstudio.bookhistory.domain.repository.BookListRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class BookListRepositorylmpl(
    private val bookListLocalDataSource: BookListLocalDataSource,
    private val bookListRemoteDataSource: BookListRemoteDataSource
) : BookListRepository {

    private val TAG: String? = BookListRepositorylmpl::class.java.simpleName

    override fun getTotalBookList(email: String): LiveData<List<BookListEntity>> {
        return bookListLocalDataSource.getTotalBookList(email)
    }

    override fun getBeforeReadBookList(email: String, reading_state: String): LiveData<List<BookListEntity>> {
        return bookListLocalDataSource.getBeforeReadBookList(email, reading_state)
    }

    override fun getReadingBookList(email: String, reading_state: String): LiveData<List<BookListEntity>> {
        return bookListLocalDataSource.getReadingBookList(email, reading_state)
    }

    override fun getEndReadBookList(email: String, reading_state: String): LiveData<List<BookListEntity>> {
        return bookListLocalDataSource.getEndReadBookList(email, reading_state)
    }

    override fun getIdxBookInfo(email: String, idx: Int, reading_state: String): LiveData<BookListEntity> {
        return bookListLocalDataSource.getIdxBookInfo(email, idx, reading_state)
    }

    override fun deleteIdxBookInfo(email: String, idx: Int): Single<Boolean> {
        return bookListRemoteDataSource.deleteIdxBookInfo(email, idx)
            .flatMap { returnValue ->
                if(returnValue.returnvalue.toBoolean()) {
                    bookListLocalDataSource.deleteIdxBookInfo(email, idx)
                        .subscribe({
                            Log.i(TAG, "삭제 성공")
                        })
                    Single.just(true)
                } else {
                    Single.just(false)
                }
            }
    }
}