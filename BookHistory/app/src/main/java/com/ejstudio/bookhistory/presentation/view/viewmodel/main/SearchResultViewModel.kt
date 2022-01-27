package com.ejstudio.bookhistory.presentation.view.viewmodel.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import com.ejstudio.bookhistory.domain.usecase.main.GetSearchBookUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.util.NetworkManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchResultViewModel(
    private val getSearchBookUseCase: GetSearchBookUseCase,
    private val networkManager: NetworkManager
) : BaseViewModel() {

    private val TAG = SearchResultViewModel::class.java.simpleName
    private val _backButton: MutableLiveData<Unit> = MutableLiveData()
    val backButton: LiveData<Unit> get() = _backButton

//    private val _goToSearchResult: MutableLiveData<Unit> = MutableLiveData()
//    val goToSearchResult: LiveData<Unit> get() = _goToSearchResult


    var inputSearch = MutableLiveData<String>() // 검색어(EditText two-way binding)
    private var currentQuery: String = "" // 현재 검색어

    private val _bookList = ArrayList<SearchBookModel.Document>() // 영화리스트
    val bookList = MutableLiveData<ArrayList<SearchBookModel.Document>>()
    private var currentPage: Int = 1



    fun backButton() {
        _backButton.value = Unit
    }

    fun searchButton() {
        if(currentQuery != inputSearch.value.toString().trim()) {
            currentPage = 1
            _bookList.clear()
            bookList.value?.clear()
        }
        currentQuery = inputSearch.value.toString().trim()
        if (currentQuery.isEmpty()) {
            // 검색어 입력해주세요 토스트띄우기
            return
        }
        if (!checkNetworkState()) return //네트워크연결 유무
        getSearchBook()
    }

    fun getSearchBook() {
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
                        // 결과없음 화면 보여주기
                    } else {
                        Log.i(TAG, it.documents.get(0).title)

                        _bookList.addAll(it.documents)
                        bookList.value = _bookList
//                        _movieList.value = movies as ArrayList<Movie>
//                        _toastMsg.value = MessageSet.SUCCESS
                    }
                }, {
                    Log.i(TAG, it.message.toString())
                })
        )
    }

    private fun checkNetworkState(): Boolean {
        return if (networkManager.checkNetworkState()) {
            true
        } else {
//            requestLocalMovies()
            false
        }
    }
}