package com.ejstudio.bookhistory.data.repository.main.booksearch.remote.remote

import android.util.Log
import com.ejstudio.bookhistory.data.api.ApiClient
import com.ejstudio.bookhistory.data.api.ApiInterface
import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import com.ejstudio.bookhistory.domain.model.RecentPopularBookModel
import com.ejstudio.bookhistory.domain.model.SaveBookInfoModel
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class SearchBookRemoteDataSourcelmpl(
    private val apiInterface: ApiInterface
) : SearchBookRemoteDataSource{
    private val TAG = SearchBookRemoteDataSourcelmpl::class.java.simpleName

    override fun getSearchBook(inputSearch: String, page: Int): Observable<SearchBookModel> {
        return apiInterface.getSearchBook(inputSearch, "accuracy", page, 10)
    }

    override fun isExistBook(email: String, isbn: String): Single<CheckTrueOrFalseModel> {
        return apiInterface.checkIsExistBook(email, isbn)
    }

    override fun insertBookInfo(email: String, bookInfo: SearchBookModel.Document): Single<SaveBookInfoModel> {
        return apiInterface.insertBookInfo(
            email,
            bookInfo.authors.get(0),
            bookInfo.contents,
            bookInfo.datetime,
            bookInfo.isbn,
            bookInfo.price.toString(),
            bookInfo.publisher,
            bookInfo.sale_price.toString(),
            bookInfo.status,
            bookInfo.thumbnail,
            bookInfo.title,
            bookInfo.translators.get(0),
            bookInfo.url
        )
    }

    override fun getRecentPopularBook(startDt: String, endDt: String, page: Int, pageSize: Int): Single<RecentPopularBookModel> {
        return apiInterface.getRecentPopularBook(ApiClient.BOOK_API_KEY, startDt, endDt, page, pageSize, ApiClient.FORMAT_JSON)
    }
}