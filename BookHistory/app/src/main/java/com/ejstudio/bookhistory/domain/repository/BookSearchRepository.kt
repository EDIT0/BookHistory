package com.ejstudio.bookhistory.domain.repository

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import com.ejstudio.bookhistory.domain.model.RecentPopularBookModel
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface BookSearchRepository {
    fun getSearchBook(inputSearch: String, page: Int) : Observable<SearchBookModel>
    fun insertRecentSearches(recentSearches: RecentSearchesEntity) : io.reactivex.rxjava3.core.Completable
    fun getRecentSearchesList(email:String) : LiveData<List<RecentSearchesEntity>>
    fun deleteRecentSearches(deleteIdx: Int) : io.reactivex.rxjava3.core.Completable
    fun totalDeleteRecentSearches(email: String) : io.reactivex.rxjava3.core.Completable
    fun addBook(email: String, bookInfo: SearchBookModel.Document) : Single<Boolean>
    fun getRecentPopularBook(startDt: String, endDt: String, page: Int, pageSize: Int) : Single<RecentPopularBookModel>
}