package com.ejstudio.bookhistory.presentation.view.viewmodel.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.domain.usecase.login.CreateEmailUserUseCase
import com.ejstudio.bookhistory.domain.usecase.login.RegisterEmailAndPasswordUseCase
import com.ejstudio.bookhistory.presentation.base.BaseViewModel
import com.ejstudio.bookhistory.util.NetworkManager
import kotlin.random.Random

class SignUp2ViewModel(
    private val createEmailUserUseCase: CreateEmailUserUseCase,
    private val registerEmailAndPasswordUseCase: RegisterEmailAndPasswordUseCase,
    private val networkManager: NetworkManager
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

    val currentTime: Long = System.currentTimeMillis()
    var protectDuplicateLoginToken = ""


    fun tosCheck() {
        _tosValue.value = !_tosValue.value.toString().toBoolean()
    }

    fun showExpandableToS() {
        _showExpandableToS.value = Unit
    }


    fun createUser() {
        if (!checkNetworkState()) return
        if(_tosValue.value.toString().toBoolean()) {
            protectDuplicateLoginToken = Random.nextInt(1, 999999).toString() + currentTime
//            protectDuplicateLoginToken = email + currentTime
            compositeDisposable.add(createEmailUserUseCase.execute(email, password.value!!, protectDuplicateLoginToken)
                .subscribe {
                    Log.i(TAG, "메시지: " + it)
                    if (it.toString().toBoolean()) {

                        registerEmailAndPassword() // 서버에 가입한 아이디 등록
                        _goToMain.value = Unit
                    } else {
                        // sign up fail
                        snackbarMessage = MessageSet.ALREADY_EXIST_USER.toString()
                        _requestSnackbarAction.value = Unit
                    }
                }
            )
        } else {
            snackbarMessage = MessageSet.CHECK_TOS.toString()
            _requestSnackbar.value = Unit
        }
    }

    fun registerEmailAndPassword() {
        Log.i(TAG, "registerEmailAndPassword() $email / ${password.value} / ${protectDuplicateLoginToken}")
//        compositeDisposable.add(registerEmailAndPasswordUseCase.execute(email, password.value!!, protectDuplicateLoginToken)
        compositeDisposable.add(registerEmailAndPasswordUseCase.execute(email, "일반이메일로그인", protectDuplicateLoginToken)
            .subscribe{

            }
        )
    }

    fun closeToSFragment() {
        _dissmissTos.value = Unit
    }

    private fun checkNetworkState(): Boolean {
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
        CHECK_TOS,
        ALREADY_EXIST_USER
    }
}