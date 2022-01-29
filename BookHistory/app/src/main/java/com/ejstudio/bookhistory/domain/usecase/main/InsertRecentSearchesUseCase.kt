package com.ejstudio.bookhistory.domain.usecase.main

import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.domain.repository.BookSearchRepository
import io.reactivex.Completable
import io.reactivex.rxjava3.core.Observable

class InsertRecentSearchesUseCase(
    private val bookSearchRepository: BookSearchRepository
) {
    fun execute(recentSearches: RecentSearchesEntity) : io.reactivex.rxjava3.core.Completable {
        return bookSearchRepository.insertRecentSearches(recentSearches)
    }
}