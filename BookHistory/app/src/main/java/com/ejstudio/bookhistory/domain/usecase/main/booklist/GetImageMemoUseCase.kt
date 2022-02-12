package com.ejstudio.bookhistory.domain.usecase.main.booklist

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.ImageMemoEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.domain.repository.BookListRepository

class GetImageMemoUseCase(
    private val bookListRepository: BookListRepository
) {
    fun execute(bookListIdx: Int) : LiveData<List<ImageMemoEntity>> {
        return bookListRepository.getImageMemo(bookListIdx)
    }
}