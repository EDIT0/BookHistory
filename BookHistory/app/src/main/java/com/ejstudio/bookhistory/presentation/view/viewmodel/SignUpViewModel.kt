package com.ejstudio.bookhistory.presentation.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.domain.usecase.SendSignUpEmailUseCase
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
    private val _successCheckNumber: MutableLiveData<Unit> = MutableLiveData()
    val successCheckNumber: LiveData<Unit> get() = _successCheckNumber
//    private val _successLogin: MutableLiveData<Unit> = MutableLiveData()
//    val successLogin: LiveData<Unit> get() = _successLogin

    var email = MutableLiveData<String>()
    var number = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    var passEmail = false

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

    fun checkNumber() {
        if(randomNum.toString() == number.value.toString()) {
            snackbarMessage = "인증되었습니다"
            _successCheckNumber.value = Unit
            passEmail = true
        } else {
            snackbarMessage = "인증번호를 다시 확인해주세요"
            _requestSnackbar.value = Unit
        }
    }

//    fun createUser() {
//        if (email.value == null || !passEmail) {
//            toastMessage = "이메일 인증을 진행해주세요"
//            _requestToast.value = Unit
//        } else if(password.value == null || !(password.value.toString().length >= 8 && password.value.toString().length <= 16)) {
//            toastMessage = "패스워드는 8~16자 이내로 설정해주세요"
//            _requestToast.value = Unit
//        } else {
//            compositeDisposable.add(createEmailUserUseCase.execute(email.value!!, password.value!!)
//                .subscribe {
//                    Log.i(TAG, "메시지: " + it)
//                    if (it.toString().toBoolean()) {
////                        _successLogin.value = Unit
//                    } else {
//                        // sign up fail
//                        toastMessage = "이미 가입된 이메일이거나 이메일 형식이 잘못되었습니다"
//                        _requestToast.value = Unit
//                    }
//                }
//            )
//        }
//    }
}