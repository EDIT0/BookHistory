package com.ejstudio.bookhistory.presentation.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.Observer
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityLoginBinding
import com.ejstudio.bookhistory.databinding.ActivityWelcomeBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.LoginViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.SplashViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val TAG: String? = LoginActivity::class.java.simpleName
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.loginViewModel = loginViewModel

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

            // 비밀번호 재설정
//            Firebase.auth.setLanguageCode("ko")
//            val emailAddress = "akdmadl34@naver.com"
//
//            Firebase.auth.sendPasswordResetEmail(emailAddress)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Log.d(TAG, "Email sent.")
//                    }
//                }
        }

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
            requestToast.observe(this@LoginActivity, Observer {
                showToast(loginViewModel.toastMessage)
            })
        }
    }

    fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun goToSignUpActivity() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    fun goToFindPasswordActivity() {
        startActivity(Intent(this, FindPasswordActivity::class.java))
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
}