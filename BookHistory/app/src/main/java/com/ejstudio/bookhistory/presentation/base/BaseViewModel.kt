package com.ejstudio.bookhistory.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isData = MutableLiveData<Boolean>(true)
    val isData: LiveData<Boolean> get() = _isData

    fun showProgress() {
        _isLoading.value = true
    }

    fun hideProgress() {
        _isLoading.value = false
    }

    fun showDataEmptyScreen() {
        _isData.value = false
    }

    fun hideDataEmptyScreen() {
        _isData.value = true
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}