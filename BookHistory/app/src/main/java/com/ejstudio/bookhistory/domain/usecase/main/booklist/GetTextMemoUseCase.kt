package com.ejstudio.bookhistory.domain.usecase.main.booklist

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.domain.repository.BookListRepository

class GetTextMemoUseCase(
    private val bookListRepository: BookListRepository
) {
    fun execute(bookListIdx: Int) : LiveData<List<TextMemoEntity>> {
        return bookListRepository.getTextMemo(bookListIdx)
    }
}