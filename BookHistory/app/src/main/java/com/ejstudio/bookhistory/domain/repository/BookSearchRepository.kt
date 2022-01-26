package com.ejstudio.bookhistory.domain.repository

import com.ejstudio.bookhistory.domain.model.SearchBookModel
import io.reactivex.rxjava3.core.Observable

interface BookSearchRepository {
    fun getSearchBook(inputSearch: String, page: Int) : Observable<SearchBookModel>
}