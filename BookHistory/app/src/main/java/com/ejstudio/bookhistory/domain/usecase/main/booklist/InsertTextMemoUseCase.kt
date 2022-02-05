package com.ejstudio.bookhistory.domain.usecase.main.booklist

import com.ejstudio.bookhistory.domain.repository.BookListRepository
import io.reactivex.rxjava3.core.Single

class InsertTextMemoUseCase(
    private val bookListRepository: BookListRepository
) {
    fun execute(bookIdx: Int, memoContents: String) : Single<Boolean> {
        return bookListRepository.insertTextMemo(bookIdx, memoContents)
    }
}