package com.ejstudio.bookhistory.presentation.view.activity.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityLoginBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.activity.main.MainActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.login.LoginViewModel
import com.ejstudio.bookhistory.util.PreferenceManager
import com.ejstudio.bookhistory.util.UserInfo
import com.kakao.sdk.auth.model.OAuthToken
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User


class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val TAG: String? = LoginActivity::class.java.simpleName
    private val loginViewModel: LoginViewModel by viewModel()
    private lateinit var manager: InputMethodManager
    private lateinit var loginPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.loginViewModel = loginViewModel
        loginPreferences = this.applicationContext.getSharedPreferences(PreferenceManager.LOGIN_INFO, Context.MODE_PRIVATE)

        keyBoardSetting()
        viewModelCallback()

        textWatcher()

        binding.btnGoogle.setOnClickListener {
            //이메일 인증
//            Firebase.auth.setLanguageCode("ko")
//            val user = Firebase.auth.currentUser
//            Log.i(TAG, user.toString())
//            user!!.sendEmailVerification()
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Log.d(TAG, "Email sent.")
//                    }
//                }
            kakaoLogin()
        }

    }

    fun keyBoardSetting() {
        manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    fun viewModelCallback() {
        with(loginViewModel) {
            goToMain.observe(this@LoginActivity, Observer {
                goToMainActivity()
            })
            goToSignUp.observe(this@LoginActivity, Observer {
                goToSignUpActivity()
            })
            goToFindPassword.observe(this@LoginActivity, Observer {
                goToFindPasswordActivity()
            })
            requestSnackbar.observe(this@LoginActivity, Observer {
                showSnackbar(loginViewModel.snackbarMessage)
                manager.hideSoftInputFromWindow(getCurrentFocus()?.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
            })
        }
    }

    fun goToMainActivity() {
        Log.i(TAG, "누가먼저?")
        var intent = Intent(this, MainActivity::class.java)
        intent.putExtra("WhatIsTheTypeOfLogin", MainActivity.NORMAL_LOGIN)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.rightin_activity, R.anim.leftout_activity)
    }

    fun goToSignUpActivity() {
        startActivity(Intent(this, SignUpActivity::class.java))
        overridePendingTransition(R.anim.rightin_activity, R.anim.not_move_activity)
    }

    fun goToFindPasswordActivity() {
        startActivity(Intent(this, FindPasswordActivity::class.java))
        overridePendingTransition(R.anim.rightin_activity, R.anim.not_move_activity)
    }

    fun textWatcher() {
        binding.etInputEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if(binding.etInputEmail.text!!.isEmpty()) {
                    binding.textInputLayoutEmail.error = "이메일을 입력해주세요."
                } else if(!binding.etInputEmail.text!!.contains("@") || !binding.etInputEmail.text!!.contains(".")) {
                    binding.textInputLayoutEmail.error = "이메일 형식이 아닙니다."
                } else if(binding.etInputEmail.text!!.contains("@") && binding.etInputEmail.text!!.contains(".")) {
                    binding.textInputLayoutEmail.helperText = " "
                }
            }

        })
    }

    fun kakaoLogin() {
        UserApiClient.instance
            .loginWithKakaoTalk(this@LoginActivity) { oAuthToken: OAuthToken?, error: Throwable? ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패 " + error.message)
                } else if (oAuthToken != null) {
                    Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.accessToken)

                    UserApiClient.instance.me { user: User?, meError: Throwable? ->
                        if (meError != null) {
                            Log.e(TAG, "사용자 정보 요청 실패", meError)
                        } else {
                            // 기존 가입 이력이 있는지 확인
                            loginViewModel.checkKakaoUserId(user?.id.toString())

                            val currentTime: Long = System.currentTimeMillis()
                            val protectDuplicateLoginToken = user?.id.toString() + currentTime

                            val editor = loginPreferences.edit()
                            editor.putString(PreferenceManager.KAKAO_USER_TOKEN, user?.id.toString())
                            editor.putBoolean(PreferenceManager.AUTO_LOGIN_KEY, true)
                            editor.putString(PreferenceManager.PROTECT_DUPLICATE_LOGIN_TOKEN, protectDuplicateLoginToken)
                            editor.remove("EMAIL");
                            editor.remove("PASSWORD");
                            editor.commit();
                            editor.apply()

                            UserInfo.email = user?.id.toString()

                            // 중복로그인 방지 토큰 서버에 저장
                            loginViewModel.updateProtectDuplicateLoginToken(user?.id.toString(), protectDuplicateLoginToken)


                            Log.i(TAG, """사용자 정보 요청 성공회원번호: ${user?.id} 이메일: ${user?.kakaoAccount?.email}""".trimIndent())
                        }
                    }
                }
            }
    }
}