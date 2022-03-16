package com.ejstudio.bookhistory.domain.usecase.main.booklist

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.domain.repository.BookListRepository

class GetReadingBookUseCase(
    private val bookListRepository: BookListRepository
) {
    fun execute(email: String, reading_state: String, pickerYear: String) : LiveData<List<BookListEntity>> {
        return bookListRepository.getReadingBookList(email, reading_state, pickerYear)
    }
}