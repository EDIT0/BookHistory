package com.ejstudio.bookhistory.presentation.view.viewmodel.main.booksearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.data.model.RecentSearchesEntity
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import com.ejstudio.bookhistory.domain.usecase.main.booksearch.DeleteRecentSearchesUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booksearch.GetRecentSearchesUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booksearch.InsertRecentSearchesUseCase
import com.ejstudio.bookhistory.domain.usecase.main.booksearch.TotalDeleteRecentSearchesUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.util.NetworkManager
import com.ejstudio.bookhistory.util.UserInfo
import io.reactivex.rxjava3.schedulers.Schedulers


class SearchViewModel(
    private val insertRecentSearchesUseCase: InsertRecentSearchesUseCase,
    private val deleteRecentSearchesUseCase: DeleteRecentSearchesUseCase,
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase,
    private val totalDeleteRecentSearchesUseCase : TotalDeleteRecentSearchesUseCase,
    private val networkManager: NetworkManager
) : BaseViewModel() {

    private val TAG = SearchViewModel::class.java.simpleName

    var snackbarMessage = String()
    private val _requestSnackbar: MutableLiveData<Unit> = MutableLiveData()
    val requestSnackbar: LiveData<Unit> get() = _requestSnackbar

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
        if(!checkNetworkState()) {
            // 네트워크 없음
            return
        }
        if(inputSearch.value.toString().isEmpty()) {
            // 빈 검색어 토스트 띄우기
            return
        }
        compositeDisposable.add(
            insertRecentSearchesUseCase.execute(RecentSearchesEntity(UserInfo.email, inputSearch.value.toString().trim()))
                .subscribeOn(Schedulers.io())
                .subscribe({
                           Log.i(TAG, "인설트 성공")
                }, {
                    Log.i(TAG, "인설트 실패 ${it.message.toString()}")
                })
        )
        _goToSearchResult.value = Unit
    }

    fun deleteRecentSearches(deleteIdx: Int) {
        compositeDisposable.add(
            deleteRecentSearchesUseCase.execute(deleteIdx)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.i(TAG, "딜리트 성공")
                }, {
                    Log.i(TAG, "딜리트 실패 ${it.message.toString()}")
                })
        )
    }

    fun totalDeleteRecentSearches() {
        compositeDisposable.add(
            totalDeleteRecentSearchesUseCase.execute(UserInfo.email)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.i(TAG, "전체 딜리트 성공")
                }, {
                    Log.i(TAG, "전체 딜리트 실패 ${it.message.toString()}")
                })
        )
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