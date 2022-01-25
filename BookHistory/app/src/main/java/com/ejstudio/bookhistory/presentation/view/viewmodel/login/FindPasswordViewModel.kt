package com.ejstudio.bookhistory.presentation.view.viewmodel.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.domain.usecase.login.CheckEmailUseCase
import com.ejstudio.bookhistory.domain.usecase.login.SendFindPasswordEmailUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class FindPasswordViewModel(
    private val checkEmailUseCase: CheckEmailUseCase,
    private val sendFindPasswordEmailUseCase: SendFindPasswordEmailUseCase
) : BaseViewModel(){

    private val TAG = FindPasswordViewModel::class.java.simpleName

    var snackbarMessage = String()
    var email = MutableLiveData<String>()

    private val _goToFindPassword2: MutableLiveData<Unit> = MutableLiveData()
    val goToFindPassword2: LiveData<Unit> get() = _goToFindPassword2
    private val _goToLogin: MutableLiveData<Unit> = MutableLiveData()
    val goToLogin: LiveData<Unit> get() = _goToLogin
    private val _requestSnackbar: MutableLiveData<Unit> = MutableLiveData()
    val requestSnackbar: LiveData<Unit> get() = _requestSnackbar
    private val _completeCheckEmail: MutableLiveData<Unit> = MutableLiveData()
    val completeCheckEmail: LiveData<Unit> get() = _completeCheckEmail

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
                Log.i(TAG, "이메일이 있는가?: ${it.returnvalue}")
                if(it.returnvalue.toBoolean()) {
                    _completeCheckEmail.value = Unit
                } else {
                    snackbarMessage = "가입되지 않은 이메일입니다."
                    _requestSnackbar.value = Unit
                }
            }, {
                Log.i(TAG, "에러: $it ${it.message}")
                snackbarMessage = "다시 시도해주세요."
                _requestSnackbar.value = Unit
            })
        )
    }

    fun sendFindPasswordEmail() {
        Log.i(TAG, "(비밀번호 찾기) 보낼 이메일 주소: "+email.value.toString())
        compositeDisposable.add(sendFindPasswordEmailUseCase.execute(email.value.toString().trim())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showProgress() }
            .doAfterTerminate {
                hideProgress()
            }
            .subscribe({
                Log.i(TAG, "이메일을 보냈는가?: ${it.toString().toBoolean()}")
                if(it.toString().toBoolean()) {
                    _goToFindPassword2.value = Unit
                } else {
                    snackbarMessage = "다시 시도해주세요."
                    _requestSnackbar.value = Unit
                }
            }, {
                Log.i(TAG, "에러: $it ${it.message}")
                snackbarMessage = "다시 시도해주세요."
                _requestSnackbar.value = Unit
            })
        )
    }


    fun goToLogin() {
        _goToLogin.value = Unit
    }
}