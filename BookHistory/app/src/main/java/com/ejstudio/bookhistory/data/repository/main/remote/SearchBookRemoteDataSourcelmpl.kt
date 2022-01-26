package com.ejstudio.bookhistory.data.repository.main.remote

import com.ejstudio.bookhistory.data.api.ApiClient
import com.ejstudio.bookhistory.data.api.ApiInterface
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import io.reactivex.rxjava3.core.Observable

class SearchBookRemoteDataSourcelmpl(
    private val apiInterface: ApiInterface
) : SearchBookRemoteDataSource{
    override fun getSearchBook(inputSearch: String, page: Int): Observable<SearchBookModel> {
        return apiInterface.getSearchBook(inputSearch, "accuracy", page, 10)
    }
}