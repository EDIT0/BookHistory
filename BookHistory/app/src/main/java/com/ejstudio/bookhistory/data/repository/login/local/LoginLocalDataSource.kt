package com.ejstudio.bookhistory.data.repository.login.local

import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.ImageMemoEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface LoginLocalDataSource {
    var isFirstWelcome: Boolean

    fun initRoomForCurrentUser(email: String) : Completable
    fun insertTotalBookList(bookList: List<BookListEntity>) : Completable
    fun insertTotalBookTextMemoList(textMemoList: List<TextMemoEntity>) : Completable
    fun insertTotalBookImageMemoList(imageMemoList: List<ImageMemoEntity>) : Completable
}