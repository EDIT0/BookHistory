package com.ejstudio.bookhistory.domain.usecase.main

import com.ejstudio.bookhistory.domain.repository.BookSearchRepository

class TotalDeleteRecentSearchesUseCase(
    private val bookSearchRepository: BookSearchRepository
) {
    fun execute(email: String) : io.reactivex.rxjava3.core.Completable {
        return bookSearchRepository.totalDeleteRecentSearches(email)
    }
}