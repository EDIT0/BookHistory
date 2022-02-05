package com.ejstudio.bookhistory.domain.usecase.main.booklist

import com.ejstudio.bookhistory.domain.repository.BookListRepository
import io.reactivex.rxjava3.core.Single

class DeleteIdxTextMemoUseCase(
    private val bookListRepository: BookListRepository
) {
    fun execute(textMemoIdx: Int) : Single<Boolean> {
        return bookListRepository.deleteIdxTextMemo(textMemoIdx)
    }
}