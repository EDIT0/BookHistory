package com.ejstudio.bookhistory.data.repository.login

import android.util.Log
import com.ejstudio.bookhistory.data.repository.login.local.LoginLocalDataSource
import com.ejstudio.bookhistory.data.repository.login.remote.LoginRemoteDataSource
import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import com.ejstudio.bookhistory.domain.repository.LoginRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginRepositorylmpl(
    private val loginLocalDataSource: LoginLocalDataSource,
    private val loginRemoteDataSource: LoginRemoteDataSource
) : LoginRepository {

    private val TAG: String? = LoginRepositorylmpl::class.java.simpleName

    override var isFirstWelcome: Boolean
        get() = loginLocalDataSource.isFirstWelcome
        set(value) {
            loginLocalDataSource.isFirstWelcome = value
        }

    override var isAutoLogin: Observable<Boolean>
        get() = loginRemoteDataSource.isAutoLogin
        set(value) {}


    override fun loginAuth(email: String, password: String): Observable<Boolean> {
        val currentTime: Long = System.currentTimeMillis()
        val protectDuplicateLoginToken = email + currentTime

        return loginRemoteDataSource.loginAuth(email, password, protectDuplicateLoginToken)
            .flatMap {
                if(it) {
                    loginRemoteDataSource.updateProtectDuplicateLoginToken(email, protectDuplicateLoginToken)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { t1, t2 ->
                            Log.i(TAG, "중복로그인 방지 로큰 저장: ${t1.returnvalue}")
                        }
                    Observable.just(it)
                } else {
                    Observable.just(it)
                }
            }
    }

    override fun sendEmail(email: String, randomNumber: String): Observable<String> {
        return loginRemoteDataSource.sendEmail(email, randomNumber)
    }

    override fun createEmailUser(email: String, password: String, protectDuplicateLoginToken: String): Observable<Boolean> {
        return loginRemoteDataSource.createEmailUser(email, password, protectDuplicateLoginToken)
    }

    override fun checkEmail(email: String): Single<CheckTrueOrFalseModel> {
        return loginRemoteDataSource.checkEmail(email)
    }

    override fun registerEmailAndPassword(email: String, password: String, protectDuplicateLoginToken: String): Observable<Unit> {
        return loginRemoteDataSource.registerEmailAndPassword(email, password, protectDuplicateLoginToken)
    }

    override fun sendFindPasswordEmail(email: String): Observable<Boolean> {
        return loginRemoteDataSource.sendFindPasswordEmail(email)
    }

    override fun updateProtectDuplicateLoginToken(userId: String, protectDuplicateLoginToken: String) {
        loginRemoteDataSource.updateProtectDuplicateLoginToken(userId, protectDuplicateLoginToken)
            .subscribe { t1, t2 ->
                Log.i(TAG, "카카오 중복로그인 방지 토큰: ${t1.returnvalue}")
            }
    }
}