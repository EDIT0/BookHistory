package com.ejstudio.bookhistory.domain.usecase.main.booklist

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.domain.repository.BookListRepository

class GetBeforeReadBookUseCase(
    private val bookListRepository: BookListRepository
) {
    fun execute(email: String, reading_state: String) : LiveData<List<BookListEntity>> {
        return bookListRepository.getReadingBookList(email, reading_state)
    }
}