package com.ejstudio.bookhistory.domain.usecase.main

import com.ejstudio.bookhistory.domain.repository.BookSearchRepository
import io.reactivex.Completable

class DeleteRecentSearchesUseCase(
    private val bookSearchRepository: BookSearchRepository
) {
    fun execute(deleteIdx: Int) : io.reactivex.rxjava3.core.Completable {
        return bookSearchRepository.deleteRecentSearches(deleteIdx)
    }
}