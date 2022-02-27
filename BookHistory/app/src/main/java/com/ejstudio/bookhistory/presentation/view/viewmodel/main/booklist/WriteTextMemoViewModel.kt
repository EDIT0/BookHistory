package com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.domain.usecase.main.booklist.InsertTextMemoUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import com.ejstudio.bookhistory.util.NetworkManager
import com.ejstudio.bookhistory.util.UserInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class WriteTextMemoViewModel(
    private val insertTextMemoUseCase: InsertTextMemoUseCase,
    private val networkManager: NetworkManager
) : BaseViewModel() {

    private val TAG = WriteTextMemoViewModel::class.java.simpleName

    var snackbarMessage = String()
    private val _requestSnackbar: MutableLiveData<Unit> = MutableLiveData()
    val requestSnackbar: LiveData<Unit> get() = _requestSnackbar

    private val _backButton: MutableLiveData<Unit> = MutableLiveData()
    val backButton: LiveData<Unit> get() = _backButton

    private val _clickOCR: MutableLiveData<Unit> = MutableLiveData()
    val clickOCR: LiveData<Unit> get() = _clickOCR

    private val _showToast: MutableLiveData<Unit> = MutableLiveData()
    val showToast: LiveData<Unit> get() = _showToast
    var toastMessage = ""

    var book_idx = 0
    var bookTitle = ""
    var memo_contents = String()

    var completed_save_memo = false

    fun backButton() {
        _backButton.value = Unit
    }

    fun saveMemo() {
        if (!checkNetworkState()) return
        if(!memo_contents.trim().equals("")) {
            Log.i(TAG, "저장할 메모: " + book_idx + " / " + memo_contents)
            compositeDisposable.add(
                insertTextMemoUseCase.execute(book_idx, memo_contents.trim())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showProgress() }
                    .doAfterTerminate {
                        hideProgress()
                        completed_save_memo = true
                        _backButton.value = Unit
                    }
                    .subscribe({
                        Log.i(TAG, "메모 작성 성공")
                    }, {
                        Log.i(TAG, "메모 작성 에러: "+ it.message.toString())
                    })
            )
        } else {
            toastMessage = "내용을 입력하세요"
            _showToast.value = Unit
        }
    }

    fun clickOCR() {
        _clickOCR.value = Unit
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
        NETWORK_NOT_CONNECTED
    }

}