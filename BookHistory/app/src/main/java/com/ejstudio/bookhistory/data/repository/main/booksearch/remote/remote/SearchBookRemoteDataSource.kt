package com.ejstudio.bookhistory.data.repository.main.booksearch.remote.remote

import com.ejstudio.bookhistory.domain.model.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface SearchBookRemoteDataSource {
    fun getSearchBook(inputSearch: String, page: Int) : Observable<SearchBookModel>
    fun isExistBook(email: String, isbn: String) : Single<CheckTrueOrFalseModel>
    fun insertBookInfo(email: String, bookInfo: SearchBookModel.Document) : Single<SaveBookInfoModel>
    fun getRecentPopularBook(startDt: String, endDt: String, page: Int, pageSize: Int): Single<RecentPopularBookModel>
    fun getRecommendBook(isbn: String) : Single<RecommendBookModel>
    fun getAlwaysPopularBook(page: Int, pageSize: Int): Single<RecentPopularBookModel>
}