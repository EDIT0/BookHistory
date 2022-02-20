package com.ejstudio.bookhistory.domain.repository

import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface LoginRepository {
    var isFirstWelcome: Boolean
    var isAutoLogin: Observable<Boolean>

    fun loginAuth(email: String, password: String) : Observable<Boolean>
    fun sendEmail(email: String, randomNumber: String): Observable<String>
    fun createEmailUser(email: String, password: String, protectDuplicateLoginToken: String) : Observable<Boolean>
    fun checkEmail(email: String) : Single<CheckTrueOrFalseModel>
    fun registerEmailAndPassword(email: String, password: String, protectDuplicateLoginToken: String) : Observable<Unit>
    fun sendFindPasswordEmail(email: String) : Observable<Boolean>
    fun updateProtectDuplicateLoginToken(userId: String, protectDuplicateLoginToken: String)
    fun getProtectDuplicateLoginTokenFromServer(email: String) : Single<CheckTrueOrFalseModel>
    fun initRoomForCurrentUser(email: String) : Completable
    fun getTotalUserInfo(email: String)
}