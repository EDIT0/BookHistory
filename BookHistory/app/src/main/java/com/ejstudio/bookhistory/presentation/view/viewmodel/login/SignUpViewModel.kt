package com.ejstudio.bookhistory.presentation.view.viewmodel.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.domain.usecase.login.SendSignUpEmailUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.random.Random

class SignUpViewModel(
    private val sendSignUpEmailUseCase: SendSignUpEmailUseCase
//    private val createEmailUserUseCase: CreateEmailUserUseCase
) : BaseViewModel() {

    private val TAG = SignUpViewModel::class.java.simpleName

    public var snackbarMessage = String()

    private val _goToLogin: MutableLiveData<Unit> = MutableLiveData()
    val goToLogin: LiveData<Unit> get() = _goToLogin
    private val _goToSignUp2: MutableLiveData<Unit> = MutableLiveData()
    val goToSignUp2: LiveData<Unit> get() = _goToSignUp2

    private val _requestSnackbar: MutableLiveData<Unit> = MutableLiveData()
    val requestSnackbar: LiveData<Unit> get() = _requestSnackbar

    var email = MutableLiveData<String>()
    var number = MutableLiveData<String>()
    var password = MutableLiveData<String>()


    var random = Random
    var randomNum = 0

    var authEmail = String()

    fun goToLogin() {
        _goToLogin.value = Unit
    }

    fun goToSignUp2() {
        _goToSignUp2.value = Unit
    }

    fun sendNumber() {
        if(email.value == null || email.value.toString() == ""){
            Log.i(TAG, "이메일 비었음")
        }else{
            Log.i(TAG, "이메일 보내기")
            randomNum = random.nextInt(100000, 999999)

            authEmail = email.value.toString()

            compositeDisposable.add(sendSignUpEmailUseCase.execute(email.value.toString(), randomNum.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doAfterTerminate {
                    hideProgress()
                }
                .subscribe({
                    Log.i(TAG, "리턴: $it")
                }, {
                    snackbarMessage = "인증번호를 확인해주세요"
                    _requestSnackbar.value = Unit
                    Log.i(TAG, "에러: $it ${it.message}")
                })
            )
        }
    }
}