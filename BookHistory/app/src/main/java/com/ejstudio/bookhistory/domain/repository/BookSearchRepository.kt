package com.ejstudio.bookhistory.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import io.reactivex.Completable
import io.reactivex.rxjava3.core.Observable

interface BookSearchRepository {
    fun getSearchBook(inputSearch: String, page: Int) : Observable<SearchBookModel>
    fun insertRecentSearches(recentSearches: RecentSearchesEntity) : io.reactivex.rxjava3.core.Completable
    fun getRecentSearchesList(email:String) : LiveData<List<RecentSearchesEntity>>
    fun deleteRecentSearches(deleteIdx: Int) : io.reactivex.rxjava3.core.Completable
    fun totalDeleteRecentSearches(email: String) : io.reactivex.rxjava3.core.Completable
}