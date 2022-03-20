package com.ejstudio.bookhistory.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.ejstudio.bookhistory.presentation.view.activity.login.LoginActivity
import com.ejstudio.bookhistory.presentation.view.activity.login.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.reactivex.rxjava3.core.Observable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class LoginManager(context: Context) : KoinComponent {

    private val TAG: String? = LoginManager::class.java.simpleName

    val loginActivity : LoginActivity by inject()
    val signUpActivity : SignUpActivity by inject()

    private val loginPreferences: SharedPreferences = context.getSharedPreferences(PreferenceManager.LOGIN_INFO, Context.MODE_PRIVATE)

    // 일반 로그인
    fun loginAuth(email: String, password: String, protectDuplicateLoginToken: String): Observable<Boolean> = Observable.create<Boolean> {
        if(email == null || password == null) {
            it.onNext(false)
            Log.i(TAG, email + " " + password);
        } else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(loginActivity) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = FirebaseAuth.getInstance().currentUser

                        // 로그인 시 중복로그인 확인을 위해 기존 내부에 저장되어 있던 토큰 값을 저장해 둔다.
                        var prefProtectDuplicateLoginToken = loginPreferences.getString(PreferenceManager.PROTECT_DUPLICATE_LOGIN_TOKEN, "")
                        UserInfo.protectDuplicateLoginToken = prefProtectDuplicateLoginToken?:""

                        val editor = loginPreferences.edit()

                        editor.putString(PreferenceManager.EMAIL, email)
                        editor.putString(PreferenceManager.PASSWORD, Converter.encByKey(Converter.key, password))
                        editor.putBoolean(PreferenceManager.AUTO_LOGIN_KEY, true)
                        editor.putString(PreferenceManager.PROTECT_DUPLICATE_LOGIN_TOKEN, protectDuplicateLoginToken)
                        editor.apply()

//                        global.email = authEmail.toString()
                        UserInfo.email = email.toString().trim()

                        Log.i(TAG, "현재 로그인한 이메일: ${email.toString()}")

                        it.onNext(task.isSuccessful)
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        it.onNext(false)
                    }
                }
        }
    }

    // 가입 후 바로 로그인
    fun createEmailUser(email:String, password: String, protectDuplicateLoginToken: String): Observable<Boolean> = Observable.create<Boolean> {
        if(email == null || password == null) {
            it.onNext(false)
        } else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(signUpActivity) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = FirebaseAuth.getInstance().currentUser
//                        global.email = email.toString()

                        UserInfo.protectDuplicateLoginToken = protectDuplicateLoginToken

                        val editor = loginPreferences.edit()
                        editor.putString(PreferenceManager.EMAIL, email)
                        editor.putString(PreferenceManager.PASSWORD, Converter.encByKey(Converter.key, password))
                        editor.putBoolean(PreferenceManager.AUTO_LOGIN_KEY, true)
                        editor.putString(PreferenceManager.PROTECT_DUPLICATE_LOGIN_TOKEN, protectDuplicateLoginToken)
                        editor.remove("KAKAO_USER_TOKEN");
                        editor.apply()

                        UserInfo.email = email.toString().trim()

                        Log.i(TAG, "현재 회원가입 -> 로그인 이메일: $email")
                        it.onNext(task.isSuccessful)
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        it.onNext(false)
                    }
                }
        }
    }

    // 비밀번호 변경 이메일 보내기
    fun sendFindPasswordEmail(email: String) : Observable<Boolean> = Observable.create<Boolean> {
        Firebase.auth.setLanguageCode("ko")
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                        it.onNext(task.isSuccessful)
                    } else {
                        it.onNext(false)
                    }
                }
    }




}