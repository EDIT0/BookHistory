package com.ejstudio.bookhistory.domain.usecase.main.mybookhistory

import android.util.Log
import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.domain.model.TextImageMemoModel
import com.ejstudio.bookhistory.domain.repository.MyBookHistoryRepository

class GetCalendarDateMemoUseCase(
    private val myBookHistoryRepository: MyBookHistoryRepository
) {
    private val TAG = GetCalendarDateMemoUseCase::class.java.simpleName

    fun execute(email: String, calendarDate: String) : LiveData<List<TextImageMemoModel>> {
        Log.i(TAG, "지켜보는 중")
        return myBookHistoryRepository.getCalendarDateMemoList(email, calendarDate)
    }
}