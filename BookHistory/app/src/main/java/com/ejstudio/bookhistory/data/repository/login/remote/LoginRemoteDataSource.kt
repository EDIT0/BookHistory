package com.ejstudio.bookhistory.data.repository.login.remote

import com.ejstudio.bookhistory.domain.model.CheckEmailModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface LoginRemoteDataSource {
    var isAutoLogin: Observable<Boolean>

    fun loginAuth(email: String, password: String) : Observable<Boolean>
    fun sendEmail(email: String, randomNumber: String): Observable<String>
    fun createEmailUser(email: String, password: String) : Observable<Boolean>
    fun checkEmail(email: String) : Single<CheckEmailModel>
    fun registerEmailAndPassword(email: String, password: String) : Observable<Unit>
    fun sendFindPasswordEmail(email: String) : Observable<Boolean>
}