package com.ejstudio.bookhistory.domain.usecase.main.booksearch

import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import com.ejstudio.bookhistory.domain.repository.BookSearchRepository
import io.reactivex.rxjava3.core.Single

class AddBookUseCase(
    private val bookSearchRepository: BookSearchRepository
) {
    fun execute(email: String, bookInfo: SearchBookModel.Document) : Single<Boolean> {
        return bookSearchRepository.addBook(email, bookInfo)
    }
}