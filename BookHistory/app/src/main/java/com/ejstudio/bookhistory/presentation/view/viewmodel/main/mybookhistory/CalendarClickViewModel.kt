package com.ejstudio.bookhistory.presentation.view.viewmodel.main.mybookhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.domain.model.TextImageMemoModel
import com.ejstudio.bookhistory.domain.usecase.main.mybookhistory.GetCalendarDateMemoUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.util.UserInfo

class CalendarClickViewModel(
    private val getCalendarDateMemoUseCase : GetCalendarDateMemoUseCase
) : BaseViewModel() {

    private val _backButton: MutableLiveData<Unit> = MutableLiveData()
    val backButton: LiveData<Unit> get() = _backButton

    var calendarDate = ""

    // 날짜에 맞는 글, 이미지 메모 리스트
    lateinit var _calendarDateMemoList: LiveData<List<TextImageMemoModel>> /*= getCalendarDateMemoUseCase.execute(UserInfo.email, "${calendarDate}")*/
    val calendarDateMemoList: LiveData<List<TextImageMemoModel>> get() = _calendarDateMemoList

    fun getCalendarDate() {
        _calendarDateMemoList = getCalendarDateMemoUseCase.execute(UserInfo.email, "${calendarDate}")
    }

    fun backButton() {
        _backButton.value = Unit
    }
}