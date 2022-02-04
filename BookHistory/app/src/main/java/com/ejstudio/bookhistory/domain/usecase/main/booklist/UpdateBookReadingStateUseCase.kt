package com.ejstudio.bookhistory.domain.usecase.main.booklist

import com.ejstudio.bookhistory.domain.repository.BookListRepository
import io.reactivex.rxjava3.core.Single

class UpdateBookReadingStateUseCase(
    private val bookListRepository: BookListRepository
) {
    fun execute(email: String, idx: Int, reading_state: String) : Single<Boolean> {
        return bookListRepository.updateBookReadingState(email, idx, reading_state)
    }
}