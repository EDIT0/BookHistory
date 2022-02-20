package com.ejstudio.bookhistory.data.repository.login.local

import com.ejstudio.bookhistory.data.db.BookListDao
import com.ejstudio.bookhistory.data.db.ImageMemoDao
import com.ejstudio.bookhistory.data.db.TextMemoDao
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.ImageMemoEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.util.PreferenceManager
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

class LoginLocalDataSourcelmpl(
    private val preferenceManager: PreferenceManager,
    private val bookListDao: BookListDao,
    private val textMemoDao: TextMemoDao,
    private val imageMemoDao: ImageMemoDao
) : LoginLocalDataSource {

    override var isFirstWelcome: Boolean
        get() = preferenceManager.isFirstWelcome
        set(value) {
            preferenceManager.isFirstWelcome = value
        }

    override fun initRoomForCurrentUser(email: String): Completable {
        return bookListDao.initRoomForCurrentUser(email)
    }

    override fun insertTotalBookList(bookList: List<BookListEntity>) : Completable {
        return bookListDao.insertTotalBookList(bookList)
    }

    override fun insertTotalBookTextMemoList(textMemoList: List<TextMemoEntity>): Completable {
        return textMemoDao.insertTotalBookTextMemoList(textMemoList)
    }

    override fun insertTotalBookImageMemoList(imageMemoList: List<ImageMemoEntity>): Completable {
        return imageMemoDao.insertTotalBookImageMemoList(imageMemoList)
    }

}