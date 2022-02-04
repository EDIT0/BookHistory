package com.ejstudio.bookhistory.presentation.view.viewmodel.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.data.api.ApiClient
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.domain.model.RecentPopularBookModel
import com.ejstudio.bookhistory.domain.model.RecommendBookModel
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import com.ejstudio.bookhistory.domain.usecase.main.booklist.GetBeforeReadBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booklist.GetEndReadBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booklist.GetReadingBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booksearch.GetAlwaysPopularBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booksearch.GetRecentPopularBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booksearch.GetRecommendBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booksearch.GetTotalBookUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.util.UserInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(
    private val getTotalBookUseCase : GetTotalBookUseCase,
    private val getBeforeReadBookUseCase: GetBeforeReadBookUseCase,
    private val getReadingBookUseCase: GetReadingBookUseCase,
    private val getEndReadBookUseCase: GetEndReadBookUseCase,
    private val getRecentPopularBookUseCase: GetRecentPopularBookUseCase,
    private val getRecommendBookUseCase : GetRecommendBookUseCase,
    private val getAlwaysPopularBookUseCase : GetAlwaysPopularBookUseCase
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
        getDate(-60) // 현 시간 기준 60일 전 날짜 구하기
        Log.i(TAG, "메인 뷰 모델 호출!!!!!!!!!!!!!!!!!!!!!!!")
        Log.i(TAG, "요청 url:  ${ApiClient.BOOK_BASE_URL}loanItemSrch?authKey=${ApiClient.BOOK_API_KEY}&startDt=$before60days" +
                "&endDt=$todayDate&page=$page&pageSize=${pageSize}&format=${ApiClient.FORMAT_JSON}" )

        _selectedMenu.value = "읽는 중" // 스타트 메뉴
        getRecentPopularBookList() // 요즘 많이 읽는 책들 호출
        getAlwaysPopularBookList() // 언제나 인기있는 책

    }

    private fun getDate(day: Int) {
        cal.time = date

        cal.add(Calendar.DAY_OF_MONTH, 0)
        todayDate = SimpleDateFormat("yyyy-MM-dd").format(cal.time)
        cal.add(Calendar.DAY_OF_MONTH, day)
        before60days = SimpleDateFormat("yyyy-MM-dd").format(cal.time)

        Log.i(TAG, "날짜 찍기 : " + before60days + " / " + todayDate)
    }

    // 책 전체 리스트
    private val _totalBookList = getTotalBookUseCase.execute(UserInfo.email)
    val totalBookList: LiveData<List<BookListEntity>> get() = _totalBookList

    // 읽기 전
    private val _beforeReadBookList = getBeforeReadBookUseCase.execute(UserInfo.email, BEFORE_READ)
    val beforeReadBookList: LiveData<List<BookListEntity>> get() = _beforeReadBookList

    // 읽는 중
    private val _readingBookList = getReadingBookUseCase.execute(UserInfo.email, READING)
    val readingBookList: LiveData<List<BookListEntity>> get() = _readingBookList

    // 읽은 후
    private val _endReadBookList = getEndReadBookUseCase.execute(UserInfo.email, END_READ)
    val endReadBookList: LiveData<List<BookListEntity>> get() = _endReadBookList

    // 요즘 많이 읽는 책들
    private val _recentPopularBookList = ArrayList<RecentPopularBookModel.Response.Doc>()
    val recentPopularBookList = MutableLiveData<ArrayList<RecentPopularBookModel.Response.Doc>>()

    // 당신을 위한 추천
    private val _recommendBookList = ArrayList<RecommendBookModel.Response.Doc>()
    val recommendBookList = MutableLiveData<ArrayList<RecommendBookModel.Response.Doc>>()

    // 언제나 인기있는 책
    private val _alwaysPopularBookList = ArrayList<RecentPopularBookModel.Response.Doc>()
    val alwaysPopularBookList = MutableLiveData<ArrayList<RecentPopularBookModel.Response.Doc>>()

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
                        Log.i(TAG, "요즘 많이 읽는 책 응답: " + it.response.docs.get(0).doc.bookname)
                        _recentPopularBookList.addAll(it.response.docs)
                        recentPopularBookList.value = _recentPopularBookList
                    }
                }, {
                    Log.i(TAG, "요즘 많이 읽는 책 응답 에러: " + it.message.toString())
                })
        )
    }

    /*
    * 가지고있는 전체 책들을 가지고와서 추천 책 요청
    * */
    fun getRecommendBookList(isbn: String) {
        Log.i(TAG, "당신을 위한 추천 응답 요청 " + isbn)
        compositeDisposable.add(
            getRecommendBookUseCase.execute(isbn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doAfterTerminate {
                    hideProgress()
                }
                .subscribe({ it ->
                    if (it.response.resultNum == 0) {
                        // TODO 응답 요청이 없으면 뭘 추천해주지?
                        getRecommendBookList("9791165341909;")
                    } else {
                        Log.i(TAG, "당신을 위한 추천 응답: " + it.response.docs.get(0).book.bookname)
                        _recommendBookList.addAll(it.response.docs)
                        recommendBookList.value = _recommendBookList
                    }
                }, {
                    Log.i(TAG, "당신을 위한 추천 응답 오류: " + it.message.toString())
                })
        )
    }

    /*
    * 언제나 인기있는 책
    * */
    fun getAlwaysPopularBookList() {
        compositeDisposable.add(
            getAlwaysPopularBookUseCase.execute(page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doAfterTerminate {
                    hideProgress()
                }
                .subscribe({ it ->
                    if (it.response.resultNum == 0) {
                    } else {
                        Log.i(TAG, "오랫동안 인기있는 책 응답: " + it.response.docs.get(0).doc.bookname)
                        _alwaysPopularBookList.addAll(it.response.docs)
                        alwaysPopularBookList.value = _alwaysPopularBookList
                    }
                }, {
                    Log.i(TAG, it.message.toString())
                    getAlwaysPopularBookList()
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