package com.ejstudio.bookhistory.presentation.view.viewmodel.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.domain.usecase.login.CheckEmailUseCase
import com.ejstudio.bookhistory.domain.usecase.login.IsLoginAuthUseCase
import com.ejstudio.bookhistory.domain.usecase.login.RegisterEmailAndPasswordUseCase
import com.ejstudio.bookhistory.domain.usecase.login.UpdateProtectDuplicateLoginTokenUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.util.NetworkManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginViewModel(
    private val isLoginAuthUseCase: IsLoginAuthUseCase,
    private val checkEmailUseCase: CheckEmailUseCase,
    private val registerEmailAndPasswordUseCase: RegisterEmailAndPasswordUseCase,
    private val updateProtectDuplicateLoginUseCase: UpdateProtectDuplicateLoginTokenUseCase,
    private val networkManager: NetworkManager
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

    fun goToMainWithEmail() {
        if (!checkNetworkState()) return
        // 인증 후 메인으로
        if(inputEmail.value == null || inputPassword.value == null) {
            snackbarMessage = MessageSet.CHECK_YOUR_ID_PASSWORD.toString()
            _requestSnackbar.value = Unit
        } else {
            Log.i(TAG, "로그인 실행한다. ${inputEmail.value} ${inputPassword.value}")
            compositeDisposable.add(isLoginAuthUseCase.execute(inputEmail.value!!.trim(), inputPassword.value!!.trim())
                .doOnSubscribe { showProgress() }
                .doAfterTerminate {
                    hideProgress()
                }
                .subscribe {
                    hideProgress()
                    Log.i(TAG, "로그인 허가 메시지: " + it)
                    if (it.toString().toBoolean()) {
                        snackbarMessage = "Login Success"
                        _requestSnackbar.value = Unit
                        _goToMain.value = Unit
                    } else {
                        snackbarMessage = MessageSet.CHECK_YOUR_ID_PASSWORD.toString()
                        _requestSnackbar.value = Unit
                    }
                })
        }
    }

    var initProtectDuplicateLoginTokenForKakao = ""
    fun checkKakaoUserId(kakaoUserId: String) {
        if (!checkNetworkState()) return
        compositeDisposable.add(checkEmailUseCase.execute(kakaoUserId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showProgress() }
            .doAfterTerminate {
                hideProgress()
            }
            .subscribe ({ it ->
                Log.e(TAG, "(카카오로그인) 이미 가입했는지? 있:true 없:false : ${it.returnvalue.toString().toBoolean()}")
                if(!it.returnvalue.toString().toBoolean()) { // 가입 이력이 없으면 false, 있으면 true
                    registerKakaoUserId(kakaoUserId)
                }
                _goToMain.value = Unit
            }, {
                Log.e(TAG, it.message.toString())
            })
        )
    }

    fun registerKakaoUserId(kakaoUserId: String) {
        compositeDisposable.add(registerEmailAndPasswordUseCase.execute(kakaoUserId, "카카오로그인", initProtectDuplicateLoginTokenForKakao)
            .subscribe {

            }
        )
    }

    fun updateProtectDuplicateLoginToken(userId: String, protectDuplicateLoginToken: String) {
        updateProtectDuplicateLoginUseCase.execute(userId, protectDuplicateLoginToken)
    }

    fun goToSignUp() {
        // 회원가입
        _goToSignUp.value = Unit
    }

    fun goToFindPassword() {
        // 회원가입
        _goToFindPassword.value = Unit
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
        NETWORK_NOT_CONNECTED,
        CHECK_YOUR_ID_PASSWORD
    }
}