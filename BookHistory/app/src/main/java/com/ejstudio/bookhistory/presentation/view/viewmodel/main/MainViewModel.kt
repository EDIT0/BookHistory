package com.ejstudio.bookhistory.presentation.view.viewmodel.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.data.api.ApiClient
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.domain.model.RecentPopularBookModel
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import com.ejstudio.bookhistory.domain.usecase.main.booklist.GetBeforeReadBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booklist.GetEndReadBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booklist.GetReadingBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booksearch.GetRecentPopularBookUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.util.UserInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(
    private val getBeforeReadBookUseCase: GetBeforeReadBookUseCase,
    private val getReadingBookUseCase: GetReadingBookUseCase,
    private val getEndReadBookUseCase: GetEndReadBookUseCase,
    private val getRecentPopularBookUseCase: GetRecentPopularBookUseCase
): BaseViewModel() {

    private val TAG = MainViewModel::class.java.simpleName

    public var _selectedMenu : MutableLiveData<String> = MutableLiveData()
    val selectedMenu: LiveData<String> get() = _selectedMenu

    var cal: Calendar = Calendar.getInstance()
    var date= Date()
    var todayDate = ""
    var before60days = ""
    var page = 1
    var pageSize = 10

    init {
        getDate(-60)
        Log.i(TAG, "메인 뷰 모델 호출!!!!!!!!!!!!!!!!!!!!!!!")
        _selectedMenu.value = "읽을 책"
        Log.i(TAG, "요청 url:  ${ApiClient.BOOK_BASE_URL}loanItemSrch?authKey=${ApiClient.BOOK_API_KEY}&startDt=$before60days" +
                "&endDt=$todayDate&page=$page&pageSize=${pageSize}&format=${ApiClient.FORMAT_JSON}" )
        getRecentPopularBookList()
    }

    private fun getDate(day: Int) {
        cal.time = date

        cal.add(Calendar.DAY_OF_MONTH, 0)
        todayDate = SimpleDateFormat("yyyy-MM-dd").format(cal.time)
        cal.add(Calendar.DAY_OF_MONTH, day)
        before60days = SimpleDateFormat("yyyy-MM-dd").format(cal.time)

        Log.i(TAG, "날짜 찍기 : " + before60days + " / " + todayDate)
    }

    // 읽기 전
    private val _beforeReadBookList = getBeforeReadBookUseCase.execute(UserInfo.email, BEFORE_READ)
    val beforeReadBookList: LiveData<List<BookListEntity>> get() = _beforeReadBookList

    // 읽는 중
    private val _readingBookList = getReadingBookUseCase.execute(UserInfo.email, READING)
    val readingBookList: LiveData<List<BookListEntity>> get() = _readingBookList

    // 읽은 후
    private val _endReadBookList = getEndReadBookUseCase.execute(UserInfo.email, END_READ)
    val endReadBookList: LiveData<List<BookListEntity>> get() = _endReadBookList

    private val _recentPopularBookList = ArrayList<RecentPopularBookModel.Response.Doc>() // 영화리스트
    val recentPopularBookList = MutableLiveData<ArrayList<RecentPopularBookModel.Response.Doc>>()

    fun changeList(state: String) {
        when (state) {
            BEFORE_READ -> _selectedMenu.value = "읽을 책"
            READING -> _selectedMenu.value = "읽는 중"
            END_READ -> _selectedMenu.value = "읽은 책"
        }
    }

    fun getRecentPopularBookList() {
        compositeDisposable.add(
            getRecentPopularBookUseCase.execute(before60days, todayDate, page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doAfterTerminate {
                    hideProgress()
                }
                .subscribe({ it ->
                    if (it.response.resultNum == 0) {
                    } else {
                        Log.i(TAG, "응답: " + it.response.docs.get(0).doc.bookname)
                        _recentPopularBookList.addAll(it.response.docs)
                        recentPopularBookList.value = _recentPopularBookList
                    }
                }, {
                    Log.i(TAG, it.message.toString())
                })
        )


    }

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