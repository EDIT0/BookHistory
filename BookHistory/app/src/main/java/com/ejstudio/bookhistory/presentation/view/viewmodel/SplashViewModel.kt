package com.ejstudio.bookhistory.presentation.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.domain.usecase.IsAutoLoginUseCase
import com.ejstudio.bookhistory.domain.usecase.IsFirstWelcomeUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel

class SplashViewModel(
    private val isFirstWelcomeUseCase: IsFirstWelcomeUseCase,
    private val isAutoLoginUseCase: IsAutoLoginUseCase
) : BaseViewModel() {

    private val TAG: String = SplashViewModel::class.java.simpleName

    private val _goToMain: MutableLiveData<Unit> = MutableLiveData()
    private val _goToWelcome: MutableLiveData<Unit> = MutableLiveData()
    private val _goToLogin: MutableLiveData<Unit> = MutableLiveData()

    val goToMain: LiveData<Unit> get() = _goToMain
    val goToWelcome: LiveData<Unit> get() = _goToWelcome
    val goToLogin: LiveData<Unit> get() = _goToLogin

    fun doSplash() {
        if(isFirstWelcomeUseCase.execute()) { // 앱을 처음 설치한 사용자인지
            _goToWelcome.value = Unit
            isFirstWelcomeUseCase.execute(false)
        } else { // 첫 설치가 아니면
            compositeDisposable.add(isAutoLoginUseCase.execute()
                .subscribe {
                    if(it.toString().toBoolean()) {
                        _goToMain.value = Unit
                    } else {
                        _goToLogin.value = Unit
                    }
                }
            )
        }
    }
}