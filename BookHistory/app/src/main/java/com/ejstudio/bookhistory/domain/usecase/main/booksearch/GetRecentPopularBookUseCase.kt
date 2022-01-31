package com.ejstudio.bookhistory.domain.usecase.main.booksearch

import com.ejstudio.bookhistory.domain.model.RecentPopularBookModel
import com.ejstudio.bookhistory.domain.repository.BookSearchRepository
import io.reactivex.rxjava3.core.Single

class GetRecentPopularBookUseCase(
    private val bookSearchRepository: BookSearchRepository
) {
    fun execute(startDt: String, endDt: String, page: Int, pageSize: Int) : Single<RecentPopularBookModel> {
        return bookSearchRepository.getRecentPopularBook(startDt, endDt, page, pageSize)
    }
}