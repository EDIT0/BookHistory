package com.ejstudio.bookhistory.data.repository.main.booksearch

import android.util.Log
import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.data.repository.main.booksearch.local.SearchBookLocalDataSource
import com.ejstudio.bookhistory.data.repository.main.booksearch.remote.remote.SearchBookRemoteDataSource
import com.ejstudio.bookhistory.domain.model.RecentPopularBookModel
import com.ejstudio.bookhistory.domain.model.RecommendBookModel
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import com.ejstudio.bookhistory.domain.repository.BookSearchRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.*

class BookSearchRepositorylmpl(
    private val searchBookLocalDataSource: SearchBookLocalDataSource,
    private val searchBookRemoteDataSource: SearchBookRemoteDataSource
) : BookSearchRepository {

    private val TAG = BookSearchRepositorylmpl::class.java.simpleName

    override fun getSearchBook(inputSearch: String, page: Int): Observable<SearchBookModel> {
        return searchBookRemoteDataSource.getSearchBook(inputSearch, page)
    }

    override fun insertRecentSearches(recentSearches: RecentSearchesEntity): io.reactivex.rxjava3.core.Completable {
        return searchBookLocalDataSource.insertRecentSearches(recentSearches)
    }

    override fun getRecentSearchesList(email: String): LiveData<List<RecentSearchesEntity>> {
        return searchBookLocalDataSource.getRecentSearchesList(email)
    }

    override fun deleteRecentSearches(deleteIdx: Int): io.reactivex.rxjava3.core.Completable {
        return searchBookLocalDataSource.deleteRecentSearches(deleteIdx)
    }

    override fun totalDeleteRecentSearches(email: String): io.reactivex.rxjava3.core.Completable {
        return searchBookLocalDataSource.totalDeleteRecentSearches(email)
    }

    override fun addBook(email: String, bookInfo: SearchBookModel.Document): Single<Boolean> {
        return searchBookRemoteDataSource.isExistBook(email, bookInfo.isbn)
            .flatMap { returnValue ->
//                Single.create { signleEmitter ->
                Log.i(TAG, "1??? ?????? " + returnValue.returnvalue)
                if(returnValue.returnvalue.toBoolean()) { // returnValue??? true??? ?????? ?????? ???????????? ????????? ???
                    Single.just(true)
                } else { // ?????? ?????????????????? ?????????
                    searchBookRemoteDataSource.insertBookInfo(email, bookInfo) //????????? ??????
                        .flatMap {
                            Log.i(TAG, "2??? ?????? " + it.idx + " / " + it.add_datetime)
                            if(!it.idx.equals("false")) {
                                Log.i(TAG, "????????? ??? ?????? ?????? ??????")
                            }
                            searchBookLocalDataSource.insertBookInfo(it.idx.toInt(), it.add_datetime, email, bookInfo) // ????????? ??????
                                .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                                .subscribe {
                                    Log.i(TAG, "????????? ??? ?????? ?????? ??????")
                                }
                            Single.just(false)
                        }
                }
//                }
            }
    }

    override fun getRecentPopularBook(startDt: String, endDt: String, page: Int, pageSize: Int): Single<RecentPopularBookModel> {
        return searchBookRemoteDataSource.getRecentPopularBook(startDt, endDt, page, pageSize)
    }

    override fun getRecommendBook(isbn: String): Single<RecommendBookModel> {
        return searchBookRemoteDataSource.getRecommendBook(isbn)
    }

    override fun getAlwaysPopularBook(page: Int, pageSize: Int): Single<RecentPopularBookModel> {
        return searchBookRemoteDataSource.getAlwaysPopularBook(page, pageSize)
    }

}