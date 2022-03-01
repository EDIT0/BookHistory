package com.ejstudio.bookhistory.data.repository.login.remote

import android.util.Log
import com.ejstudio.bookhistory.data.api.ApiInterface
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import com.ejstudio.bookhistory.domain.model.TotalBookImageMemoListModel
import com.ejstudio.bookhistory.domain.model.TotalBookListModel
import com.ejstudio.bookhistory.domain.model.TotalBookTextMemoListModel
import com.ejstudio.bookhistory.util.Converter
import com.ejstudio.bookhistory.util.LoginManager
import com.ejstudio.bookhistory.util.PreferenceManager
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Single
import java.lang.Exception

class LoginRemoteDataSourcelmpl(
    private val preferenceManager: PreferenceManager,
    private val loginManager : LoginManager,
    private val apiInterface: ApiInterface
) : LoginRemoteDataSource {

    private val TAG = LoginRemoteDataSourcelmpl::class.java.simpleName

    var encText: String = ""
    var encProtectDuplicateLoginToken: String = ""

    override var isAutoLogin: Observable<Boolean>
        get() = preferenceManager.isAutoLogin
        set(value) {}

    override fun loginAuth(email: String, password: String, protectDuplicateLoginToken: String): Observable<Boolean> {
        return loginManager.loginAuth(email, password, protectDuplicateLoginToken)
    }

    override fun sendEmail(email: String, randomNumber: String): Observable<String> {
        try {
            encText = Converter.encByKey(Converter.key, email)!!
//            decText = Converter.decByKey(Converter.key, encText)!!
            Log.i(TAG, "이메일 암호화 결과 : $encText")
//            Log.i(TAG, "이메일 복호화 결과 : $decText")
        } catch (e: Exception) {
            Log.i(TAG, "이메일 암호화 에러: " + e.printStackTrace())
        }

        return apiInterface.emailSender(encText, randomNumber)
    }

    override fun createEmailUser(email: String, password: String, protectDuplicateLoginToken: String): Observable<Boolean> {
        return loginManager.createEmailUser(email, password, protectDuplicateLoginToken)
    }

    override fun checkEmail(email: String): Single<CheckTrueOrFalseModel> {
        try {
            encText = Converter.encByKey(Converter.key, email)!!
//            decText = Converter.decByKey(Converter.key, encText)!!
            Log.i(TAG, "이메일 암호화 결과 : $encText")
//            Log.i(TAG, "이메일 복호화 결과 : $decText")
        } catch (e: Exception) {
            Log.i(TAG, "이메일 암호화 에러: " + e.printStackTrace())
        }

        return apiInterface.checkEmail(encText)
    }

    override fun registerEmailAndPassword(email: String, password: String, protectDuplicateLoginToken: String): Observable<Unit> {
        try {
            encText = Converter.encByKey(Converter.key, email)!!
//            decText = Converter.decByKey(Converter.key, encText)!!
            Log.i(TAG, "이메일 암호화 결과 : $encText")
//            Log.i(TAG, "이메일 복호화 결과 : $decText")
        } catch (e: Exception) {
            Log.i(TAG, "이메일 암호화 에러: " + e.printStackTrace())
        }
        return apiInterface.registerEmailAndPassword(encText, password, protectDuplicateLoginToken)
    }

    override fun sendFindPasswordEmail(email: String): Observable<Boolean> {
        return loginManager.sendFindPasswordEmail(email)
    }

    override fun updateProtectDuplicateLoginToken(email: String, protectDuplicateLoginToken: String): Single<CheckTrueOrFalseModel> {
        try {
            encText = Converter.encByKey(Converter.key, email)!!
            encProtectDuplicateLoginToken = Converter.encByKey(Converter.key, protectDuplicateLoginToken)!!
//            decText = Converter.decByKey(Converter.key, encText)!!
            Log.i(TAG, "이메일 암호화 결과 : $encText")
            Log.i(TAG, "중복로그인방지토큰 암호화 결과 : $encProtectDuplicateLoginToken")
//            Log.i(TAG, "이메일 복호화 결과 : $decText")
        } catch (e: Exception) {
            Log.i(TAG, "이메일, 중복로그인방지토큰 암호화 에러: " + e.printStackTrace())
        }
        return apiInterface.updateProtectDuplicateLoginToken(encText, encProtectDuplicateLoginToken)
    }

    override fun getProtectDuplicateLoginTokenFromServer(email: String): Single<CheckTrueOrFalseModel> {
        try {
            encText = Converter.encByKey(Converter.key, email)!!
//            decText = Converter.decByKey(Converter.key, encText)!!
            Log.i(TAG, "이메일 암호화 결과 : $encText")
//            Log.i(TAG, "이메일 복호화 결과 : $decText")
        } catch (e: Exception) {
            Log.i(TAG, "이메일 암호화 에러: " + e.printStackTrace())
        }
        return apiInterface.getProtectDuplicateLoginTokenFromServer(encText)
    }

    override fun getEmailTotalBookList(email: String): Single<TotalBookListModel> {
        try {
            encText = Converter.encByKey(Converter.key, email)!!
//            decText = Converter.decByKey(Converter.key, encText)!!
            Log.i(TAG, "이메일 암호화 결과 : $encText")
//            Log.i(TAG, "이메일 복호화 결과 : $decText")
        } catch (e: Exception) {
            Log.i(TAG, "이메일 암호화 에러: " + e.printStackTrace())
        }
        return apiInterface.getEmailTotalBookList(encText)
    }

    override fun getEmailTotalBookTextMemoList(email: String): Single<TotalBookTextMemoListModel> {
        try {
            encText = Converter.encByKey(Converter.key, email)!!
//            decText = Converter.decByKey(Converter.key, encText)!!
            Log.i(TAG, "이메일 암호화 결과 : $encText")
//            Log.i(TAG, "이메일 복호화 결과 : $decText")
        } catch (e: Exception) {
            Log.i(TAG, "이메일 암호화 에러: " + e.printStackTrace())
        }
        return apiInterface.getEmailTotalBookTextMemoList(encText)
    }

    override fun getEmailTotalBookImageMemoList(email: String): Single<TotalBookImageMemoListModel> {
        try {
            encText = Converter.encByKey(Converter.key, email)!!
//            decText = Converter.decByKey(Converter.key, encText)!!
            Log.i(TAG, "이메일 암호화 결과 : $encText")
//            Log.i(TAG, "이메일 복호화 결과 : $decText")
        } catch (e: Exception) {
            Log.i(TAG, "이메일 암호화 에러: " + e.printStackTrace())
        }
        return apiInterface.getEmailTotalBookImageMemoList(encText)
    }


}