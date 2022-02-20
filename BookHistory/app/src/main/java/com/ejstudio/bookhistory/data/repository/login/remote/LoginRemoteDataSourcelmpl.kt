package com.ejstudio.bookhistory.data.repository.login.remote

import com.ejstudio.bookhistory.data.api.ApiInterface
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import com.ejstudio.bookhistory.domain.model.TotalBookImageMemoListModel
import com.ejstudio.bookhistory.domain.model.TotalBookListModel
import com.ejstudio.bookhistory.domain.model.TotalBookTextMemoListModel
import com.ejstudio.bookhistory.util.LoginManager
import com.ejstudio.bookhistory.util.PreferenceManager
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Single

class LoginRemoteDataSourcelmpl(
    private val preferenceManager: PreferenceManager,
    private val loginManager : LoginManager,
    private val apiInterface: ApiInterface
) : LoginRemoteDataSource {

    private val TAG = LoginRemoteDataSourcelmpl::class.java.simpleName

    override var isAutoLogin: Observable<Boolean>
        get() = preferenceManager.isAutoLogin
        set(value) {}

    override fun loginAuth(email: String, password: String, protectDuplicateLoginToken: String): Observable<Boolean> {
        return loginManager.loginAuth(email, password, protectDuplicateLoginToken)
    }

    override fun sendEmail(email: String, randomNumber: String): Observable<String> {
        return apiInterface.emailSender(email, randomNumber)
    }

    override fun createEmailUser(email: String, password: String, protectDuplicateLoginToken: String): Observable<Boolean> {
        return loginManager.createEmailUser(email, password, protectDuplicateLoginToken)
    }

    override fun checkEmail(email: String): Single<CheckTrueOrFalseModel> {
        return apiInterface.checkEmail(email)
    }

    override fun registerEmailAndPassword(email: String, password: String, protectDuplicateLoginToken: String): Observable<Unit> {
        return apiInterface.registerEmailAndPassword(email, password, protectDuplicateLoginToken)
    }

    override fun sendFindPasswordEmail(email: String): Observable<Boolean> {
        return loginManager.sendFindPasswordEmail(email)
    }

    override fun updateProtectDuplicateLoginToken(email: String, protectDuplicateLoginToken: String): Single<CheckTrueOrFalseModel> {
        return apiInterface.updateProtectDuplicateLoginToken(email, protectDuplicateLoginToken)
    }

    override fun getProtectDuplicateLoginTokenFromServer(email: String): Single<CheckTrueOrFalseModel> {
        return apiInterface.getProtectDuplicateLoginTokenFromServer(email)
    }

    override fun getEmailTotalBookList(email: String): Single<TotalBookListModel> {
        return apiInterface.getEmailTotalBookList(email)
    }

    override fun getEmailTotalBookTextMemoList(email: String): Single<TotalBookTextMemoListModel> {
        return apiInterface.getEmailTotalBookTextMemoList(email)
    }

    override fun getEmailTotalBookImageMemoList(email: String): Single<TotalBookImageMemoListModel> {
        return apiInterface.getEmailTotalBookImageMemoList(email)
    }


}