package com.ejstudio.bookhistory.presentation.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.domain.usecase.IsLoginAuthUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel

class LoginViewModel(
    private val isLoginAuthUseCase: IsLoginAuthUseCase
) : BaseViewModel() {

    private val TAG: String? = LoginViewModel::class.java.simpleName

    public var snackbarMessage = String()

    var inputEmail = MutableLiveData<String>()
    var inputPassword = MutableLiveData<String>()

    private val _goToMain: MutableLiveData<Unit> = MutableLiveData()
    val goToMain: LiveData<Unit> get() = _goToMain
    private val _goToSignUp: MutableLiveData<Unit> = MutableLiveData()
    val goToSignUp: LiveData<Unit> get() = _goToSignUp
    private val _goToFindPassword: MutableLiveData<Unit> = MutableLiveData()
    val goToFindPassword: LiveData<Unit> get() = _goToFindPassword
    private val _requestSnackbar: MutableLiveData<Unit> = MutableLiveData()
    val requestSnackbar: LiveData<Unit> get() = _requestSnackbar

    fun goToMain() {
        // 인증 후 메인으로
        if(inputEmail.value == null || inputPassword.value == null) {
            snackbarMessage = "이메일 또는 비밀번호를 확인해주세요"
            _requestSnackbar.value = Unit
        } else {
            Log.i(TAG, "로그인 실행한다. ${inputEmail.value} ${inputPassword.value}")
            compositeDisposable.add(isLoginAuthUseCase.execute(inputEmail.value!!, inputPassword.value!!)
                .subscribe {
                    Log.i(TAG, "로그인 허가 메시지: " + it)
                    if (it.toString().toBoolean()) {
                        snackbarMessage = "Login Success"
                        _requestSnackbar.value = Unit
                        _goToMain.value = Unit
                    } else {
                        snackbarMessage = "이메일 또는 비밀번호를 확인해주세요"
                        _requestSnackbar.value = Unit
                    }
                })
        }
    }

    fun goToSignUp() {
        // 회원가입
        _goToSignUp.value = Unit
    }

    fun goToFindPassword() {
        // 회원가입
        _goToFindPassword.value = Unit
    }
}