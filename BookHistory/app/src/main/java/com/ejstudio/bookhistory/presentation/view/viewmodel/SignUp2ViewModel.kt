package com.ejstudio.bookhistory.presentation.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.domain.usecase.CreateEmailUserUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel

class SignUp2ViewModel(
    private val createEmailUserUseCase: CreateEmailUserUseCase
) : BaseViewModel() {

    private val TAG: String? = SignUp2ViewModel::class.java.simpleName

    public var snackbarMessage = String()

    private val _requestSnackbar: MutableLiveData<Unit> = MutableLiveData()
    val requestSnackbar: LiveData<Unit> get() = _requestSnackbar

    private val _goToMain: MutableLiveData<Unit> = MutableLiveData()
    val goToMain: LiveData<Unit> get() = _goToMain

    var email = String()
    var password = MutableLiveData<String>()
    var checkPassword = MutableLiveData<String>()


    fun createUser() {
//        if(password.value == null || !(password.value.toString().length >= 8 && password.value.toString().length <= 16)) {
//            toastMessage = "패스워드는 8~16자 이내로 설정해주세요"
//            _requestToast.value = Unit
//        } else {
            compositeDisposable.add(createEmailUserUseCase.execute(email, password.value!!)
                .subscribe {
                    Log.i(TAG, "메시지: " + it)
                    if (it.toString().toBoolean()) {
//                        _successLogin.value = Unit
                        _goToMain.value = Unit
                    } else {
                        // sign up fail
                        snackbarMessage = "이미 가입된 이메일입니다."
                        _requestSnackbar.value = Unit
                    }
                }
            )
//        }
    }

}