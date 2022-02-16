package com.ejstudio.bookhistory.domain.repository

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.domain.model.TextImageMemoModel

interface MyBookHistoryRepository {
    fun getEmailTotalTextImageMemoList(email: String) : LiveData<List<TextImageMemoModel>>
    fun getCalendarDateMemoList(email: String, calendarDate: String) : LiveData<List<TextImageMemoModel>>
}