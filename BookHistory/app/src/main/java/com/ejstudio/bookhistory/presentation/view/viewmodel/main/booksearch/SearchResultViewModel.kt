package com.ejstudio.bookhistory.presentation.view.viewmodel.main.booksearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import com.ejstudio.bookhistory.domain.usecase.main.booksearch.GetSearchBookUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booksearch.InsertRecentSearchesUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.util.NetworkManager
import com.ejstudio.bookhistory.util.UserInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchResultViewModel(
    private val getSearchBookUseCase: GetSearchBookUseCase,
    private val insertRecentSearchesUseCase: InsertRecentSearchesUseCase,
    private val networkManager: NetworkManager
) : BaseViewModel() {

    private val TAG = SearchResultViewModel::class.java.simpleName

    var snackbarMessage = String()
    private val _requestSnackbar: MutableLiveData<Unit> = MutableLiveData()
    val requestSnackbar: LiveData<Unit> get() = _requestSnackbar

    private val _backButton: MutableLiveData<Unit> = MutableLiveData()
    val backButton: LiveData<Unit> get() = _backButton

    private val _keyHidden: MutableLiveData<Unit> = MutableLiveData()
    val keyHidden: LiveData<Unit> get() = _keyHidden

//    private val _goToSearchResult: MutableLiveData<Unit> = MutableLiveData()
//    val goToSearchResult: LiveData<Unit> get() = _goToSearchResult


    var inputSearch = MutableLiveData<String>() // 검색어(EditText two-way binding)
    private var currentQuery: String = "" // 현재 검색어

    private val _bookList = ArrayList<SearchBookModel.Document>() // 영화리스트
    val bookList = MutableLiveData<ArrayList<SearchBookModel.Document>>()
    private var currentPage: Int = 1
    private var isEnd = false


    fun backButton() {
        _backButton.value = Unit
    }

    fun searchButton() {
        if (!checkNetworkState()) return //네트워크연결 유무

        _keyHidden.value = Unit

        if(currentQuery != inputSearch.value.toString().trim()) {
            currentPage = 1
            _bookList.clear()
            bookList.value?.clear()
            isEnd = false

            /*
            * 최근 검색어 저장
            * currentQuery가 ""가 아니면 새로 검색된 것이라 추가해준다.
            * */
            if(!currentQuery.equals("")) {
                compositeDisposable.add(
                    insertRecentSearchesUseCase.execute(RecentSearchesEntity(UserInfo.email, inputSearch.value.toString().trim()))
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            Log.i(TAG, "인설트 성공")
                        }, {
                            Log.i(TAG, "인설트 실패 ${it.message.toString()}")
                        })
                )
            }
        }
        currentQuery = inputSearch.value.toString().trim()
        if (currentQuery.isEmpty()) {
            // 검색어 입력해주세요 토스트띄우기
            return
        }
        getSearchBook()
    }

    fun getSearchBook() {
        if (!checkNetworkState()) return //네트워크연결 유무
        if(!isEnd) {
            compositeDisposable.add(
                getSearchBookUseCase.execute(currentQuery, currentPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showProgress() }
                    .doAfterTerminate {
                        hideProgress()
                        currentPage++
                    }
                    .subscribe({ it ->
                        if (it.documents.isEmpty()) {
                            showDataEmptyScreen()
                        } else {
                            hideDataEmptyScreen()
                            Log.i(TAG, it.documents.get(0).title + "마지막 페이지: " + it.meta.is_end)

                            _bookList.addAll(it.documents)
                            bookList.value = _bookList
                            isEnd = it.meta.is_end // 마지막 페이지 여부
//                        _movieList.value = movies as ArrayList<Movie>
//                        _toastMsg.value = MessageSet.SUCCESS
                        }
                    }, {
                        Log.i(TAG, it.message.toString())
                    })
            )
        }
    }

    fun checkNetworkState(): Boolean {
        return if (networkManager.checkNetworkState()) {
            true
        } else {
            snackbarMessage = MessageSet.NETWORK_NOT_CONNECTED.toString()
            _requestSnackbar.value = Unit
            false
        }
    }

    enum class MessageSet {
        NETWORK_NOT_CONNECTED
    }
}