package com.ejstudio.bookhistory.domain.usecase.main.booksearch

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.domain.repository.BookListRepository
import com.ejstudio.bookhistory.domain.repository.BookSearchRepository

class GetTotalBookUseCase(
    private val bookListRepository: BookListRepository
) {
    fun execute(email: String) : LiveData<List<BookListEntity>> {
        return bookListRepository.getTotalBookList(email)
    }
}