package com.ejstudio.bookhistory.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.ejstudio.bookhistory.presentation.view.activity.LoginActivity
import com.ejstudio.bookhistory.presentation.view.activity.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.rxjava3.core.Observable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class LoginManager(context: Context) : KoinComponent {

    private val TAG: String? = LoginManager::class.java.simpleName

    val loginActivity : LoginActivity by inject()
    val signUpActivity : SignUpActivity by inject()

    private val loginPreferences: SharedPreferences = context.getSharedPreferences(PreferenceManager.LOGIN_INFO, Context.MODE_PRIVATE)

    fun loginAuth(email: String, password: String): Observable<Boolean> = Observable.create<Boolean> {
        if(email == null || password == null) {
            it.onNext(false)
            Log.i(TAG, email + " " + password);
        } else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(loginActivity) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = FirebaseAuth.getInstance().currentUser

                        val editor = loginPreferences.edit()
                        editor.putString(PreferenceManager.EMAIL, email)
                        editor.putString(PreferenceManager.PASSWORD, password)
                        editor.putBoolean(PreferenceManager.AUTO_LOGIN_KEY, true)
                        editor.apply()

//                        global.email = authEmail.toString()

                        Log.i(TAG, "현재 로그인한 이메일: ${email.toString()}")

                        it.onNext(task.isSuccessful)
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        it.onNext(false)
                    }
                }
        }
    }


    fun createEmailUser(email:String, password: String): Observable<Boolean> = Observable.create<Boolean> {
        if(email == null || password == null) {
            it.onNext(false)
        } else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(signUpActivity) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = FirebaseAuth.getInstance().currentUser
//                        global.email = email.toString()

                        val editor = loginPreferences.edit()
                        editor.putString(PreferenceManager.EMAIL, email)
                        editor.putString(PreferenceManager.PASSWORD, password)
                        editor.putBoolean(PreferenceManager.AUTO_LOGIN_KEY, true)
                        editor.apply()

                        Log.i(TAG, "현재 회원가입 -> 로그인 이메일: $email")
                        it.onNext(task.isSuccessful)
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        it.onNext(false)
                    }
                }
        }
    }



}