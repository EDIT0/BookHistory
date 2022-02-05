package com.ejstudio.bookhistory.domain.usecase.main.booklist

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.domain.repository.BookListRepository

class GetIdxTextMemoUseCase(
    private val bookListRepository: BookListRepository
) {
    fun execute(textMemoIdx: Int) : LiveData<TextMemoEntity> {
        return bookListRepository.getIdxTextMemo(textMemoIdx)
    }
}