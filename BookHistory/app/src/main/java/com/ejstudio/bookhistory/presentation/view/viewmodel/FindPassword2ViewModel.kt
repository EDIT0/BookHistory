package com.ejstudio.bookhistory.presentation.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.presentation.base.BaseViewModel

class FindPassword2ViewModel() : BaseViewModel() {
    private val _goToLogin: MutableLiveData<Unit> = MutableLiveData()
    val goToLogin: LiveData<Unit> get() = _goToLogin

    fun goToLogin() {
        _goToLogin.value = Unit
    }
}