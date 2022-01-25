package com.ejstudio.bookhistory.domain.usecase.login

import android.util.Log
import com.ejstudio.bookhistory.domain.repository.LoginRepository
import io.reactivex.rxjava3.core.Observable

class RegisterEmailAndPasswordUseCase(
    private val loginRepository: LoginRepository
) {
    private val TAG = RegisterEmailAndPasswordUseCase::class.java.simpleName

    fun execute(email: String, password: String) : Observable<Unit> {
        Log.i(TAG, "호출")
        return loginRepository.registerEmailAndPassword(email, password)
    }
}