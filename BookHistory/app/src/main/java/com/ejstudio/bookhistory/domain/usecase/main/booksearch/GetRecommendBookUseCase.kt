package com.ejstudio.bookhistory.domain.usecase.main.booksearch

import com.ejstudio.bookhistory.domain.model.RecentPopularBookModel
import com.ejstudio.bookhistory.domain.model.RecommendBookModel
import com.ejstudio.bookhistory.domain.repository.BookSearchRepository
import io.reactivex.rxjava3.core.Single

class GetRecommendBookUseCase(
    private val bookSearchRepository: BookSearchRepository
) {
    fun execute(isbn: String) : Single<RecommendBookModel> {
        return bookSearchRepository.getRecommendBook(isbn)
    }
}