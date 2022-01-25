package com.ejstudio.bookhistory.presentation.view.viewmodel.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.domain.usecase.login.CreateEmailUserUseCase
import com.ejstudio.bookhistory.domain.usecase.login.RegisterEmailAndPasswordUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel

class SignUp2ViewModel(
    private val createEmailUserUseCase: CreateEmailUserUseCase,
    private val registerEmailAndPasswordUseCase: RegisterEmailAndPasswordUseCase
) : BaseViewModel() {

    private val TAG: String? = SignUp2ViewModel::class.java.simpleName

    public var snackbarMessage = String()

    private val _requestSnackbar: MutableLiveData<Unit> = MutableLiveData()
    val requestSnackbar: LiveData<Unit> get() = _requestSnackbar
    private val _requestSnackbarAction: MutableLiveData<Unit> = MutableLiveData()
    val requestSnackbarAction: LiveData<Unit> get() = _requestSnackbarAction

    private val _goToMain: MutableLiveData<Unit> = MutableLiveData()
    val goToMain: LiveData<Unit> get() = _goToMain

    private val _tosValue: MutableLiveData<Boolean> = MutableLiveData()
    val tosValue: LiveData<Boolean> get() = _tosValue

    private val _showExpandableToS: MutableLiveData<Unit> = MutableLiveData()
    val showExpandableToS: LiveData<Unit> get() = _showExpandableToS

    private val _dissmissTos: MutableLiveData<Unit> = MutableLiveData()
    val dissmissTos: LiveData<Unit> get() = _dissmissTos

    var email = String()
    var password = MutableLiveData<String>()
    var checkPassword = MutableLiveData<String>()


    fun tosCheck() {
        _tosValue.value = !_tosValue.value.toString().toBoolean()
    }

    fun showExpandableToS() {
        _showExpandableToS.value = Unit
    }


    fun createUser() {
        if(_tosValue.value.toString().toBoolean()) {
            compositeDisposable.add(createEmailUserUseCase.execute(email, password.value!!)
                .subscribe {
                    Log.i(TAG, "메시지: " + it)
                    if (it.toString().toBoolean()) {
                        registerEmailAndPassword() // 서버에 가입한 아이디 등록
                        _goToMain.value = Unit
                    } else {
                        // sign up fail
                        snackbarMessage = "이미 가입된 이메일입니다."
                        _requestSnackbarAction.value = Unit
                    }
                }
            )
        } else {
            snackbarMessage = "이용약관에 체크해주세요."
            _requestSnackbar.value = Unit
        }
    }

    fun registerEmailAndPassword() {
        Log.i(TAG, "registerEmailAndPassword() $email / ${password.value}")
        compositeDisposable.add(registerEmailAndPasswordUseCase.execute(email, password.value!!)
            .subscribe{

            }
        )
    }

    fun closeToSFragment() {
        _dissmissTos.value = Unit
    }
}