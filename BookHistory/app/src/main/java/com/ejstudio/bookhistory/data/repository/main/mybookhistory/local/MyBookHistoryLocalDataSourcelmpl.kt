package com.ejstudio.bookhistory.data.repository.main.mybookhistory.local

import android.util.Log
import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.db.TextMemoDao
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.domain.model.TextImageMemoModel

class MyBookHistoryLocalDataSourcelmpl(
    private val textMemoDao: TextMemoDao
) : MyBookHistoryLocalDataSource {
    private val TAG: String? = MyBookHistoryLocalDataSource::class.java.simpleName

    override fun getEmailTotalTextImageMemoList(email: String): LiveData<List<TextImageMemoModel>> {
        return textMemoDao.getEmailTotalTextImageMemo(email)
    }

    override fun getCalendarDateMemoList(email: String, calendarDate: String): LiveData<List<TextImageMemoModel>> {
        Log.i(TAG, "호출 날짜 ${calendarDate}")
        return textMemoDao.getCalendarDateMemoList(email, calendarDate)
    }
}