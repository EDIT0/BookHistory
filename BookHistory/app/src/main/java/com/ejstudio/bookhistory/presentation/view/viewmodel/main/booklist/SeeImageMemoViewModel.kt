package com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.domain.usecase.main.booklist.DeleteIdxImageMemoUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.util.NetworkManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class SeeImageMemoViewModel(
    private val deleteIdxImageMemoUseCase: DeleteIdxImageMemoUseCase,
    private val networkManager: NetworkManager
) :BaseViewModel() {

    private val TAG = SeeImageMemoViewModel::class.java.simpleName

    var snackbarMessage = String()
    private val _requestSnackbar: MutableLiveData<Unit> = MutableLiveData()
    val requestSnackbar: LiveData<Unit> get() = _requestSnackbar

    private val _backButton: MutableLiveData<Unit> = MutableLiveData()
    val backButton: LiveData<Unit> get() = _backButton

    private val _deleteImageMemo: MutableLiveData<Unit> = MutableLiveData()
    val deleteImageMemo: LiveData<Unit> get() = _deleteImageMemo


    var imageMemoIdx = 0
    var imageUrl = MutableLiveData<String>()
    var bookTitle = ""

    fun backButton() {
        _backButton.value = Unit
    }

    fun deleteImageMemo() {
        _deleteImageMemo.value = Unit
    }

    fun deleteIdxImageMemo() {
        if (!checkNetworkState()) return
        deleteIdxImageMemoUseCase.execute(imageMemoIdx)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showProgress() }
            .doAfterTerminate {
                hideProgress()
                _backButton.value = Unit
            }
            .subscribe({
                Log.i(TAG, "이미지 메모 삭제 성공")
            }, {
                Log.i(TAG, "이미지 메모 삭제 에러: " + it.message.toString())
            })
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