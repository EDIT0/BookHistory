package com.ejstudio.bookhistory.data.repository.login

import com.ejstudio.bookhistory.data.repository.login.local.LoginLocalDataSource
import com.ejstudio.bookhistory.data.repository.login.remote.LoginRemoteDataSource
import com.ejstudio.bookhistory.domain.model.CheckEmailModel
import com.ejstudio.bookhistory.domain.repository.LoginRepository
import io.reactivex.rxjava3.core.Observable

class LoginRepositorylmpl(
    private val loginLocalDataSource: LoginLocalDataSource,
    private val loginRemoteDataSource: LoginRemoteDataSource
) : LoginRepository {

    override var isFirstWelcome: Boolean
        get() = loginLocalDataSource.isFirstWelcome
        set(value) {
            loginLocalDataSource.isFirstWelcome = value
        }

    override var isAutoLogin: Observable<Boolean>
        get() = loginRemoteDataSource.isAutoLogin
        set(value) {}


    override fun loginAuth(email: String, password: String): Observable<Boolean> {
        return loginRemoteDataSource.loginAuth(email, password)
    }

    override fun sendEmail(email: String, randomNumber: String): Observable<String> {
        return loginRemoteDataSource.sendEmail(email, randomNumber)
    }

    override fun createEmailUser(email: String, password: String): Observable<Boolean> {
        return loginRemoteDataSource.createEmailUser(email, password)
    }

    override fun checkEmail(email: String): Observable<CheckEmailModel> {
        return loginRemoteDataSource.checkEmail(email)
    }

    override fun registerEmailAndPassword(email: String, password: String): Observable<Unit> {
        return loginRemoteDataSource.registerEmailAndPassword(email, password)
    }
}