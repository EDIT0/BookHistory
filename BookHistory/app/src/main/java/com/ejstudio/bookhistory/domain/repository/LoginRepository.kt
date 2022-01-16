package com.ejstudio.bookhistory.domain.repository

import io.reactivex.rxjava3.core.Observable

interface LoginRepository {
    var isFirstWelcome: Boolean
    var isAutoLogin: Observable<Boolean>

    fun loginAuth(email: String, password: String) : Observable<Boolean>
    fun sendEmail(email: String, randomNumber: String): Observable<String>
    fun createEmailUser(email: String, password: String) : Observable<Boolean>
}