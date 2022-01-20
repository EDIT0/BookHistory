package com.ejstudio.bookhistory.presentation.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.domain.usecase.CheckEmailUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.system.exitProcess

class FindPasswordViewModel(
    private val checkEmailUseCase: CheckEmailUseCase
) : BaseViewModel(){

    private val TAG = FindPasswordViewModel::class.java.simpleName

    var email = MutableLiveData<String>()

    private val _goToFindPassword2: MutableLiveData<Unit> = MutableLiveData()
    val goToFindPassword2: LiveData<Unit> get() = _goToFindPassword2
    private val _goToLogin: MutableLiveData<Unit> = MutableLiveData()
    val goToLogin: LiveData<Unit> get() = _goToLogin

    fun goToFindPassword2() {
        Log.i(TAG, "버튼눌림 ")
        compositeDisposable.add(checkEmailUseCase.execute(email.value.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showProgress() }
            .doAfterTerminate {
                hideProgress()
            }
            .subscribe({
                Log.i(TAG, "리턴: ${it.returnvalue}")
                if(it.returnvalue.toBoolean()) {
                    Log.i(TAG, "리턴:1 ${it.returnvalue}")
                    _goToFindPassword2.value = Unit
                } else {
                    // 등록되지 않은 이메일
                    goToLogin()
                }
            }, {
                Log.i(TAG, "에러: $it ${it.message}")
            })
        )



    }

    fun goToLogin() {
        _goToLogin.value = Unit
    }
}