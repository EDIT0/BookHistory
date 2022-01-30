package com.ejstudio.bookhistory.data.repository.main.booksearch.local

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.db.BookListDao
import com.ejstudio.bookhistory.data.db.RecentSearchesDao
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel

class SearchBookLocalDataSourcelmpl(
    private val recentSaechesDao: RecentSearchesDao,
    private val bookListDao: BookListDao
) : SearchBookLocalDataSource {

    private val TAG: String? = SearchBookLocalDataSourcelmpl::class.java.simpleName

    override fun insertRecentSearches(recentSearches: RecentSearchesEntity): io.reactivex.rxjava3.core.Completable {
        return recentSaechesDao.insertRecentSearches(recentSearches)
    }

    override fun getRecentSearchesList(email: String): LiveData<List<RecentSearchesEntity>> {
        return recentSaechesDao.getAllRecentSearches(email)
    }

    override fun deleteRecentSearches(deleteIdx: Int): io.reactivex.rxjava3.core.Completable {
        return recentSaechesDao.deleteRecentSearches(deleteIdx)
    }

    override fun totalDeleteRecentSearches(email: String): io.reactivex.rxjava3.core.Completable {
        return recentSaechesDao.deleteAllRecentSearches(email)
    }

    override fun insertBookInfo(idx: Int, add_datetime: String, email: String, bookInfo: SearchBookModel.Document): io.reactivex.rxjava3.core.Completable {
        return bookListDao.insertBook(
            BookListEntity(
                idx,
                email,
                bookInfo.title,
                bookInfo.contents,
                bookInfo.url,
                bookInfo.isbn,
                bookInfo.datetime,
                bookInfo.authors.get(0),
                bookInfo.publisher,
                bookInfo.translators.get(0),
                bookInfo.price.toString(),
                bookInfo.sale_price.toString(),
                bookInfo.thumbnail,
                bookInfo.status,
                reading_state = MainViewModel.BEFORE_READ,
                add_datetime = add_datetime,
                reading_start_datetime = "",
                reading_end_datetime = ""
            )
        )
    }
}