package com.ejstudio.bookhistory.data.repository.login.remote

import android.util.Log
import com.ejstudio.bookhistory.data.api.ApiInterface
import com.ejstudio.bookhistory.domain.model.CheckEmailModel
import com.ejstudio.bookhistory.util.LoginManager
import com.ejstudio.bookhistory.util.PreferenceManager
import io.reactivex.rxjava3.core.Observable

class LoginRemoteDataSourcelmpl(
    private val preferenceManager: PreferenceManager,
    private val loginManager : LoginManager,
    private val apiInterface: ApiInterface
) : LoginRemoteDataSource {

    private val TAG = LoginRemoteDataSourcelmpl::class.java.simpleName

    override var isAutoLogin: Observable<Boolean>
        get() = preferenceManager.isAutoLogin
        set(value) {}

    override fun loginAuth(email: String, password: String): Observable<Boolean> {
        return loginManager.loginAuth(email, password)
    }

    override fun sendEmail(email: String, randomNumber: String): Observable<String> {
        return apiInterface.emailSender(email, randomNumber)
    }

    override fun createEmailUser(email: String, password: String): Observable<Boolean> {
        return loginManager.createEmailUser(email, password)
    }

    override fun checkEmail(email: String): Observable<CheckEmailModel> {
        return apiInterface.checkEmail(email)
    }

    override fun registerEmailAndPassword(email: String, password: String): Observable<Unit> {
        return apiInterface.registerEmailAndPassword(email, password)
    }

}