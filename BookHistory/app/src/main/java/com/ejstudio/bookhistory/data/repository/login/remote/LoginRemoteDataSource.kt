package com.ejstudio.bookhistory.data.repository.login.remote

import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import com.ejstudio.bookhistory.domain.model.TotalBookImageMemoListModel
import com.ejstudio.bookhistory.domain.model.TotalBookListModel
import com.ejstudio.bookhistory.domain.model.TotalBookTextMemoListModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface LoginRemoteDataSource {
    var isAutoLogin: Observable<Boolean>

    fun loginAuth(email: String, password: String, protectDuplicateLoginToken: String) : Observable<Boolean>
    fun sendEmail(email: String, randomNumber: String): Observable<String>
    fun createEmailUser(email: String, password: String, protectDuplicateLoginToken: String) : Observable<Boolean>
    fun checkEmail(email: String) : Single<CheckTrueOrFalseModel>
    fun registerEmailAndPassword(email: String, password: String, protectDuplicateLoginToken: String) : Observable<Unit>
    fun sendFindPasswordEmail(email: String) : Observable<Boolean>
    fun updateProtectDuplicateLoginToken(email: String, protectDuplicateLoginToken: String) : Single<CheckTrueOrFalseModel>
    fun getProtectDuplicateLoginTokenFromServer(email: String) : Single<CheckTrueOrFalseModel>
    fun getEmailTotalBookList(email: String) : Single<TotalBookListModel>
    fun getEmailTotalBookTextMemoList(email: String) : Single<TotalBookTextMemoListModel>
    fun getEmailTotalBookImageMemoList(email: String) : Single<TotalBookImageMemoListModel>
}