package com.ejstudio.bookhistory.presentation.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityLoginBinding
import com.ejstudio.bookhistory.databinding.ActivitySignUpBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.LoginViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.SignUpViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {

    private val signUpViewModel: SignUpViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.signUpViewModel = signUpViewModel

        viewModelCallback()
    }

    fun viewModelCallback() {
        with(signUpViewModel) {
            requestToast.observe(this@SignUpActivity, Observer {
                showToast(signUpViewModel.toastMessage)
            })
            // 이메일 인증 성공
            successCheckNumber.observe(this@SignUpActivity, Observer {
                binding.etInputEmail.isClickable =false
                binding.etInputEmail.isFocusable = false
                binding.btnSendNumber.isClickable = false
                binding.btnSendNumber.isFocusable = false
                showToast(signUpViewModel.toastMessage)
            })
            // 로그인 성공
            successLogin.observe(this@SignUpActivity, Observer {
                goToMainActivity()
                finish()
            })
        }
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}