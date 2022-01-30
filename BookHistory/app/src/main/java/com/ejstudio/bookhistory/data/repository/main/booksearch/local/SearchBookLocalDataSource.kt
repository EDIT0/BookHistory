package com.ejstudio.bookhistory.data.repository.main.booksearch.local

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.domain.model.SearchBookModel

interface SearchBookLocalDataSource {
    fun insertRecentSearches(recentSearches: RecentSearchesEntity) :io.reactivex.rxjava3.core.Completable
    fun getRecentSearchesList(email: String) : LiveData<List<RecentSearchesEntity>>
    fun deleteRecentSearches(deleteIdx: Int) : io.reactivex.rxjava3.core.Completable
    fun totalDeleteRecentSearches(email: String) : io.reactivex.rxjava3.core.Completable
    fun insertBookInfo(idx: Int, add_datetiem: String, email: String, bookInfo: SearchBookModel.Document) : io.reactivex.rxjava3.core.Completable
}