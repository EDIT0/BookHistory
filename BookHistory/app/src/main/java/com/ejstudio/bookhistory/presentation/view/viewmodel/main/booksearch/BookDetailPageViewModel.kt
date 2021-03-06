package com.ejstudio.bookhistory.presentation.view.viewmodel.main.booksearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import com.ejstudio.bookhistory.domain.usecase.main.booksearch.AddBookUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.util.NetworkManager
import com.ejstudio.bookhistory.util.UserInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class BookDetailPageViewModel(
    private val addBookUseCase: AddBookUseCase,
    private val networkManager: NetworkManager
) : BaseViewModel() {
    private val TAG: String? = BookDetailPageViewModel::class.java.simpleName

    var authors = String()
    var contents = String()
    var datetime = String()
    var isbn = String()
    var price = String()
    var publisher = String()
    var sale_price = String()
    var status = String()
    var thumbnail = String()
    var title = String()
    var translators = String()
    var url = String()

    private val _backButton: MutableLiveData<Unit> = MutableLiveData()
    val backButton: LiveData<Unit> get() = _backButton

    var snackbarMessage = String()
    private val _requestSnackbar: MutableLiveData<Unit> = MutableLiveData()
    val requestSnackbar: LiveData<Unit> get() = _requestSnackbar

    private val _contentsSeeDetail: MutableLiveData<Unit> = MutableLiveData()
    val contentsSeeDetail: LiveData<Unit> get() = _contentsSeeDetail


    fun backButton() {
        _backButton.value = Unit
    }

    fun contentsSeeDetail() {
        _contentsSeeDetail.value = Unit
    }

    fun addBook() {
        if (!checkNetworkState()) return

        val bookInfo = SearchBookModel.Document(
            listOf(authors),
            contents,
            datetime,
            isbn,
            price.toInt(),
            publisher,
            sale_price.toInt(),
            status,
            thumbnail,
            title,
            listOf(translators),
            url
        )

        compositeDisposable.add(
            addBookUseCase.execute(UserInfo.email, bookInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doAfterTerminate {
                    hideProgress()
                }
                .subscribe({ it ->
                    Log.i(TAG, "?????? ???????????????? ${it}")
                    if(it) {
                        // ?????? ?????? ????????? ????????? ?????????
                        snackbarMessage = MessageSet.ALREADY_GET_BOOK.toString()
                        _requestSnackbar.value = Unit
                    } else {
                        // ????????? ??? ??????
                        snackbarMessage = MessageSet.COMPLETE_ADD_BOOK.toString()
                        _requestSnackbar.value = Unit
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
            snackbarMessage = MessageSet.NETWORK_NOT_CONNECTED.toString()
            _requestSnackbar.value = Unit
            false
        }
    }

    enum class MessageSet {
        NETWORK_NOT_CONNECTED,
        COMPLETE_ADD_BOOK,
        ALREADY_GET_BOOK
    }
}