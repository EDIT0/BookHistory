package com.ejstudio.bookhistory.domain.usecase.main.booklist

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.domain.repository.BookListRepository

class GetIdxBookInfoUseCase(
    private val bookListRepository: BookListRepository
) {
    fun execute(email: String, idx: Int, reading_state: String) : LiveData<BookListEntity> {
        return bookListRepository.getIdxBookInfo(email, idx, reading_state)
    }
}