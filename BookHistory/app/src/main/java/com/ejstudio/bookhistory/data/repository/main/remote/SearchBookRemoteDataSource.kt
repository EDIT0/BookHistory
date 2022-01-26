package com.ejstudio.bookhistory.data.repository.main.remote

import com.ejstudio.bookhistory.domain.model.SearchBookModel
import io.reactivex.rxjava3.core.Observable

interface SearchBookRemoteDataSource {
    fun getSearchBook(inputSearch: String, page: Int) : Observable<SearchBookModel>
}