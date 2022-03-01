package com.ejstudio.bookhistory.data.repository.main.setting.remote

import android.util.Log
import com.ejstudio.bookhistory.data.api.ApiInterface
import com.ejstudio.bookhistory.data.repository.login.remote.LoginRemoteDataSourcelmpl
import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import com.ejstudio.bookhistory.util.Converter
import io.reactivex.rxjava3.core.Single
import java.lang.Exception

class SettingRemoteDataSourcelmpl(
    private val apiInterface: ApiInterface
) : SettingRemoteDataSource {

    private val TAG = SettingRemoteDataSourcelmpl::class.java.simpleName

    var encText: String = ""

    override fun removeUserAccount(email: String): Single<CheckTrueOrFalseModel> {
        try {
            encText = Converter.encByKey(Converter.key, email)!!
//            decText = Converter.decByKey(Converter.key, encText)!!
            Log.i(TAG, "이메일 암호화 결과 : $encText")
//            Log.i(TAG, "이메일 복호화 결과 : $decText")
        } catch (e: Exception) {
            Log.i(TAG, "이메일 암호화 에러: " + e.printStackTrace())
        }

        return apiInterface.removeUserAccount(encText)
    }
}