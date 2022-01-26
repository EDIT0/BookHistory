package com.ejstudio.bookhistory.domain.usecase.main

import com.ejstudio.bookhistory.domain.model.SearchBookModel
import com.ejstudio.bookhistory.domain.repository.BookSearchRepository
import io.reactivex.rxjava3.core.Observable

class GetSearchBookUseCase(
    private val bookSearchRepository: BookSearchRepository
) {
    fun execute(inputSearch: String, page: Int): Observable<SearchBookModel> {
        return bookSearchRepository.getSearchBook(inputSearch, page)
    }
}