package com.ejstudio.bookhistory.presentation.view.viewmodel.main.booksearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import com.ejstudio.bookhistory.domain.usecase.main.booksearch.AddBookUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.util.UserInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class BookDetailPageViewModel(
    private val addBookUseCase: AddBookUseCase
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
                    Log.i(TAG, "값이 무엇인가요? ${it}")
                    if(it) {
                        // 이미 책을 가지고 있다고 알려줌
                        snackbarMessage = "이미 가지고 있는 책입니다."
                        _requestSnackbar.value = Unit
                    } else {
                        // 서버에 책 추가
                        snackbarMessage = "서재에 책을 담았습니다."
                        _requestSnackbar.value = Unit
                    }
                }, {
                    Log.i(TAG, it.message.toString())
                })
        )
    }
}