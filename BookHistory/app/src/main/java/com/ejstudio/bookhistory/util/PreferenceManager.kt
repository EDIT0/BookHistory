package com.ejstudio.bookhistory.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.ejstudio.bookhistory.presentation.view.activity.SplashActivity
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.rxjava3.core.Observable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PreferenceManager(context: Context) : KoinComponent {

    val splashActivity : SplashActivity by inject()

    companion object {
        const val LOGIN_INFO = "LOGIN_INFO"
        const val AUTO_LOGIN_KEY = "AUTO_LOGIN_KEY"
        const val IS_FIRST_KEY = "IS_FIRST_KEY"
        const val EMAIL = "EMAIL"
        const val PASSWORD = "PASSWORD"
        const val KAKAO_USER_TOKEN = "KAKAO_USER_TOKEN"
    }

    private val TAG = PreferenceManager::class.java.simpleName

    private val loginPreferences: SharedPreferences = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE)

    var isFirstWelcome: Boolean
        get() = loginPreferences.getBoolean(IS_FIRST_KEY, true)
        set(value) {
            val editor = loginPreferences.edit()
            editor.putBoolean(IS_FIRST_KEY, value)
            editor.apply()
        }

    var isAutoLogin: Observable<Boolean> = Observable.create<Boolean> {

        var prefEmail = loginPreferences.getString(EMAIL, null)
        var prefPassword = loginPreferences.getString(PASSWORD, null)
        var prefKakaoToken = loginPreferences.getString(KAKAO_USER_TOKEN, null)


        Log.i(TAG, "${prefEmail}\n${prefPassword}\n${prefKakaoToken}")

        if(prefEmail == null || prefPassword == null) {
            if(prefKakaoToken != null) {
                it.onNext(true)
            } else {
                it.onNext(false)
            }
        } else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(prefEmail, prefPassword)
                .addOnCompleteListener(splashActivity) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = FirebaseAuth.getInstance().currentUser
//                        global.email = check_email
                        it.onNext(task.isSuccessful)
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        it.onNext(false)
                    }
                }
        }
    }

}