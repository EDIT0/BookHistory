package com.ejstudio.bookhistory.presentation.view.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.domain.usecase.main.booklist.GetBeforeReadBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booklist.GetEndReadBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booklist.GetReadingBookUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.util.UserInfo

class MainViewModel(
    private val getBeforeReadBookUseCase: GetBeforeReadBookUseCase,
    private val getReadingBookUseCase: GetReadingBookUseCase,
    private val getEndReadBookUseCase: GetEndReadBookUseCase
): BaseViewModel() {

    private val _showMenuSelection: MutableLiveData<Unit> = MutableLiveData()
    val showMenuSelection: LiveData<Unit> get() = _showMenuSelection


    // 읽기 전
    private val _beforeReadBookList = getBeforeReadBookUseCase.execute(UserInfo.email, BEFORE_READ)
    val beforeReadBookList: LiveData<List<BookListEntity>> get() = _beforeReadBookList

    // 읽는 중
    private val _readingBookList = getReadingBookUseCase.execute(UserInfo.email, READING)
    val readingBookList: LiveData<List<BookListEntity>> get() = _readingBookList

    // 읽은 후
    private val _endReadBookList = getEndReadBookUseCase.execute(UserInfo.email, END_READ)
    val endReadBookList: LiveData<List<BookListEntity>> get() = _endReadBookList

//    private val _goToSearch: MutableLiveData<Unit> = MutableLiveData()
//    val goToSearch: LiveData<Unit> get() = _goToSearch
//
//
//
//    fun goToSearch() {
//        _goToSearch.value = Unit
//    }

    companion object {
        const val BEFORE_READ = "BEFORE_READ"
        const val READING = "READING"
        const val END_READ = "END_READ"
    }
}