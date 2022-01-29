package com.ejstudio.bookhistory.presentation.view.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.presentation.base.BaseViewModel

class BookDetailPageViewModel(

) : BaseViewModel() {
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

    fun backButton() {
        _backButton.value = Unit
    }
}