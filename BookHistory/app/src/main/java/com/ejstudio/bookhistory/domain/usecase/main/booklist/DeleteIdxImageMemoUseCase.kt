package com.ejstudio.bookhistory.domain.usecase.main.booklist

import com.ejstudio.bookhistory.domain.repository.BookListRepository
import io.reactivex.rxjava3.core.Single

class DeleteIdxImageMemoUseCase(
    private val bookListRepository: BookListRepository
) {
    fun execute(imageMemoIdx: Int) : Single<Boolean> {
        return bookListRepository.deleteIdxImageMemo(imageMemoIdx)
    }
}