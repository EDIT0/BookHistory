package com.ejstudio.bookhistory.data.repository.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.data.repository.main.local.SearchBookLocalDataSource
import com.ejstudio.bookhistory.data.repository.main.remote.SearchBookRemoteDataSource
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import com.ejstudio.bookhistory.domain.repository.BookSearchRepository
import io.reactivex.Completable
import io.reactivex.rxjava3.core.Observable

class BookSearchRepositorylmpl(
    private val searchBookLocalDataSource: SearchBookLocalDataSource,
    private val searchBookRemoteDataSource: SearchBookRemoteDataSource
) : BookSearchRepository {
    override fun getSearchBook(inputSearch: String, page: Int): Observable<SearchBookModel> {
        return searchBookRemoteDataSource.getSearchBook(inputSearch, page)
    }

    override fun insertRecentSearches(recentSearches: RecentSearchesEntity): Completable {
        return searchBookLocalDataSource.insertRecentSearches(recentSearches)
    }

    override fun getRecentSearchesList(email: String): LiveData<List<RecentSearchesEntity>> {
        return searchBookLocalDataSource.getRecentSearchesList(email)
    }

}