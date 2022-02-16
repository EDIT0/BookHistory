package com.ejstudio.bookhistory.data.repository.main.mybookhistory.local

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.domain.model.TextImageMemoModel

interface MyBookHistoryLocalDataSource {
    fun getEmailTotalTextImageMemoList(email: String) : LiveData<List<TextImageMemoModel>>
    fun getCalendarDateMemoList(email: String, calendarDate: String) : LiveData<List<TextImageMemoModel>>
}