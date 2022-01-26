package com.ejstudio.bookhistory.presentation.view.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.presentation.base.BaseViewModel

class MainViewModel(

): BaseViewModel() {

    private val _goToSearch: MutableLiveData<Unit> = MutableLiveData()
    val goToSearch: LiveData<Unit> get() = _goToSearch



    fun goToSearch() {
        _goToSearch.value = Unit
    }
}