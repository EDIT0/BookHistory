package com.ejstudio.bookhistory.data.repository.main.booksearch.remote.remote

import android.util.Log
import com.ejstudio.bookhistory.data.api.ApiClient
import com.ejstudio.bookhistory.data.api.ApiInterface
import com.ejstudio.bookhistory.domain.model.*
import com.ejstudio.bookhistory.util.Converter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.lang.Exception
import kotlin.random.Random

class SearchBookRemoteDataSourcelmpl(
    private val apiInterface: ApiInterface
) : SearchBookRemoteDataSource{
    private val TAG = SearchBookRemoteDataSourcelmpl::class.java.simpleName

    var encText: String = ""

    override fun getSearchBook(inputSearch: String, page: Int): Observable<SearchBookModel> {
        return apiInterface.getSearchBook(inputSearch, "accuracy", page, 10)
    }

    override fun isExistBook(email: String, isbn: String): Single<CheckTrueOrFalseModel> {
        try {
            encText = Converter.encByKey(Converter.key, email)!!
//            decText = Converter.decByKey(Converter.key, encText)!!
            Log.i(TAG, "이메일 암호화 결과 : $encText")
//            Log.i(TAG, "이메일 복호화 결과 : $decText")
        } catch (e: Exception) {
            Log.i(TAG, "이메일 암호화 에러: " + e.printStackTrace())
        }
        return apiInterface.checkIsExistBook(encText, isbn)
    }

    override fun insertBookInfo(email: String, bookInfo: SearchBookModel.Document): Single<SaveBookInfoModel> {

        try {
            encText = Converter.encByKey(Converter.key, email)!!
//            decText = Converter.decByKey(Converter.key, encText)!!
            Log.i(TAG, "이메일 암호화 결과 : $encText")
//            Log.i(TAG, "이메일 복호화 결과 : $decText")
        } catch (e: Exception) {
            Log.i(TAG, "이메일 암호화 에러: " + e.printStackTrace())
        }

        return apiInterface.insertBookInfo(
            encText,
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

    override fun getRecommendBook(isbn: String): Single<RecommendBookModel> {
        return apiInterface.getRecommendBook(ApiClient.BOOK_API_KEY, isbn, ApiClient.FORMAT_JSON)
    }

    override fun getAlwaysPopularBook(page: Int, pageSize: Int): Single<RecentPopularBookModel> {
        return apiInterface.getAlwaysPopularBook(ApiClient.BOOK_API_KEY, page, pageSize, ApiClient.FORMAT_JSON)
    }
}