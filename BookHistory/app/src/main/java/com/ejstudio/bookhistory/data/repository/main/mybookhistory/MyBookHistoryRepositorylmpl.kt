package com.ejstudio.bookhistory.data.repository.main.mybookhistory

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.data.repository.main.mybookhistory.local.MyBookHistoryLocalDataSource
import com.ejstudio.bookhistory.data.repository.main.mybookhistory.remote.MyBookHistoryRemoteDataSource
import com.ejstudio.bookhistory.domain.model.TextImageMemoModel
import com.ejstudio.bookhistory.domain.repository.MyBookHistoryRepository

class MyBookHistoryRepositorylmpl(
    private val myBookHistoryLocalDataSource: MyBookHistoryLocalDataSource,
    private val myBookHistoryRemoteDataSource: MyBookHistoryRemoteDataSource
) : MyBookHistoryRepository {

    private val TAG = MyBookHistoryRepository::class.java.simpleName

    override fun getEmailTotalTextImageMemoList(email: String, calendarYear: String): LiveData<List<TextImageMemoModel>> {
        return myBookHistoryLocalDataSource.getEmailTotalTextImageMemoList(email, calendarYear)
    }

    override fun getCalendarDateMemoList(email: String, calendarDate: String): LiveData<List<TextImageMemoModel>> {
        return myBookHistoryLocalDataSource.getCalendarDateMemoList(email, calendarDate)
    }
}