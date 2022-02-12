package com.ejstudio.bookhistory.domain.usecase.main.booklist

import com.ejstudio.bookhistory.domain.repository.BookListRepository
import io.reactivex.rxjava3.core.Single
import java.io.File

class InsertImageMemoUseCase(
    private val bookListRepository: BookListRepository
) {
    fun execute(bookIdx: Int, file: File, fileName: String) : Single<Boolean> {
        return bookListRepository.insertImageMemo(bookIdx, file, fileName)
    }
}