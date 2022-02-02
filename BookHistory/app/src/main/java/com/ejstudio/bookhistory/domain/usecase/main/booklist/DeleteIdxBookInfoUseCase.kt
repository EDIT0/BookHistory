package com.ejstudio.bookhistory.domain.usecase.main.booklist

import com.ejstudio.bookhistory.domain.repository.BookListRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class DeleteIdxBookInfoUseCase(
    private val bookListRepository: BookListRepository
) {
    fun execute(email: String, idx: Int) : Single<Boolean> {
        return bookListRepository.deleteIdxBookInfo(email, idx)
    }
}