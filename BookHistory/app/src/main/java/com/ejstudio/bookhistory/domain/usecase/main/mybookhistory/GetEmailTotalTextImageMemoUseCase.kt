package com.ejstudio.bookhistory.domain.usecase.main.mybookhistory

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.domain.model.TextImageMemoModel
import com.ejstudio.bookhistory.domain.repository.MyBookHistoryRepository

class GetEmailTotalTextImageMemoUseCase(
    private val myBookHistoryRepository: MyBookHistoryRepository
) {
    fun execute(email: String, calendarYear: String) : LiveData<List<TextImageMemoModel>> {
        return myBookHistoryRepository.getEmailTotalTextImageMemoList(email, calendarYear)
    }
}