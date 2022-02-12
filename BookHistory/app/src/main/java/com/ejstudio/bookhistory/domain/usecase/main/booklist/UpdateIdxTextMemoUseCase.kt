package com.ejstudio.bookhistory.domain.usecase.main.booklist

import com.ejstudio.bookhistory.domain.repository.BookListRepository
import io.reactivex.rxjava3.core.Single

class UpdateIdxTextMemoUseCase(
    private val bookListRepository: BookListRepository
) {
    fun execute(textMemoIdx: Int, edit_memo_contents: String) : Single<Boolean> {
        return bookListRepository.updateIdxTextMemo(textMemoIdx, edit_memo_contents)
    }
}