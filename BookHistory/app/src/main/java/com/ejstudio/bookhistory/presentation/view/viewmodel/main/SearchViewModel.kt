package com.ejstudio.bookhistory.presentation.view.viewmodel.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import com.ejstudio.bookhistory.domain.usecase.main.GetRecentSearchesUseCase
import com.ejstudio.bookhistory.domain.usecase.main.InsertRecentSearchesUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.util.NetworkManager
import com.ejstudio.bookhistory.util.UserInfo
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class SearchViewModel(
    private val insertRecentSearchesUseCase: InsertRecentSearchesUseCase,
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase,
    private val networkManager: NetworkManager
) : BaseViewModel() {

    private val TAG = SearchViewModel::class.java.simpleName
    private val _backButton: MutableLiveData<Unit> = MutableLiveData()
    val backButton: LiveData<Unit> get() = _backButton

    private val _goToSearchResult: MutableLiveData<Unit> = MutableLiveData()
    val goToSearchResult: LiveData<Unit> get() = _goToSearchResult


    var inputSearch = MutableLiveData<String>() // 검색어(EditText two-way binding)
    private var currentQuery: String = "" // 현재 검색어

    private val _bookList = ArrayList<SearchBookModel.Document>()
    val bookList = MutableLiveData<ArrayList<SearchBookModel.Document>>()
    private var currentPage: Int = 1

    private val _recentSearches = ArrayList<RecentSearchesEntity>()

//    private val _recentSearchesList = getRecentSearchesUseCase.execute(UserInfo.email)
//    val recentSearchesList: LiveData<ArrayList<RecentSearchesEntity>> get() = recentSearchesList.setValue(_recentSearchesList)

//    private val _movieList = MutableLiveData<MutableList<Movie>>() // 영화리스트
    private val _recentSearchesList = getRecentSearchesUseCase.execute(UserInfo.email)
//    val movieList: LiveData<MutableList<Movie>> get() = _movieList
    val recentSearchesList: LiveData<List<RecentSearchesEntity>> get() = _recentSearchesList



    fun backButton() {
        _backButton.value = Unit
    }

    fun searchButton() {
        if(inputSearch.value.toString().isEmpty()) {
            // 빈 검색어 토스트 띄우기
            return
        }
//        _recentSearches.add(RecentSearchesEntity(UserInfo.email, inputSearch.value.toString().trim()))
        insertRecentSearchesUseCase.execute(RecentSearchesEntity(UserInfo.email, inputSearch.value.toString().trim()))
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .subscribe({
                       Log.i(TAG, "인설트 성공")
            }, {
                Log.i(TAG, "인설트 실패 ${it.message.toString()}")
            })
        _goToSearchResult.value = Unit
    }



//    fun getSearchBook() {
//        compositeDisposable.add(
//            getSearchBookUseCase.execute(currentQuery, currentPage)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe { showProgress() }
//                .doAfterTerminate {
//                    hideProgress()
//                    currentPage++
//                }
//                .subscribe({ it ->
//                    if (it.documents.isEmpty()) {
//                        // 결과없음 화면 보여주기
//                    } else {
//                        Log.i(TAG, it.documents.get(0).title)
//
//                        _bookList.addAll(it.documents)
//                        bookList.value = _bookList
////                        _movieList.value = movies as ArrayList<Movie>
////                        _toastMsg.value = MessageSet.SUCCESS
//                    }
//                }, {
//                    Log.i(TAG, it.message.toString())
//                })
//        )
//    }

    private fun checkNetworkState(): Boolean {
        return if (networkManager.checkNetworkState()) {
            true
        } else {
//            requestLocalMovies()
            false
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}

//class SearchViewModel(
//    private val getSearchBookUseCase: GetSearchBookUseCase,
//    private val networkManager: NetworkManager
//) : BaseViewModel() {
//
//    private val TAG = SearchViewModel::class.java.simpleName
//    private val _backButton: MutableLiveData<Unit> = MutableLiveData()
//    val backButton: LiveData<Unit> get() = _backButton
//
//
//    var inputSearch = MutableLiveData<String>() // 검색어(EditText two-way binding)
//    private var currentQuery: String = "" // 현재 검색어
//
//    private val _bookList = ArrayList<SearchBookModel.Document>() // 영화리스트
//    val bookList = MutableLiveData<ArrayList<SearchBookModel.Document>>()
//    private var currentPage: Int = 1
//
//    fun backButton() {
//        _backButton.value = Unit
//    }
//
//    fun searchButton() {
//        if(currentQuery != inputSearch.value.toString().trim()) {
//            currentPage = 1
//            _bookList.clear()
//            bookList.value?.clear()
//        }
//        currentQuery = inputSearch.value.toString().trim()
//        if (currentQuery.isEmpty()) {
//            // 검색어 입력해주세요 토스트띄우기
//            return
//        }
//        if (!checkNetworkState()) return //네트워크연결 유무
//        getSearchBook()
//    }
//
//    fun getSearchBook() {
//        compositeDisposable.add(
//            getSearchBookUseCase.execute(currentQuery, currentPage)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe { showProgress() }
//                .doAfterTerminate {
//                    hideProgress()
//                    currentPage++
//                }
//                .subscribe({ it ->
//                    if (it.documents.isEmpty()) {
//                        // 결과없음 화면 보여주기
//                    } else {
//                        Log.i(TAG, it.documents.get(0).title)
//
//                        _bookList.addAll(it.documents)
//                        bookList.value = _bookList
////                        _movieList.value = movies as ArrayList<Movie>
////                        _toastMsg.value = MessageSet.SUCCESS
//                    }
//                }, {
//                    Log.i(TAG, it.message.toString())
//                })
//        )
//    }
//
//    private fun checkNetworkState(): Boolean {
//        return if (networkManager.checkNetworkState()) {
//            true
//        } else {
////            requestLocalMovies()
//            false
//        }
//    }
//}