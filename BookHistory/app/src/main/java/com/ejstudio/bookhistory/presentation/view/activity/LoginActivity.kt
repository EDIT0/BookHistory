package com.ejstudio.bookhistory.presentation.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityLoginBinding
import com.ejstudio.bookhistory.databinding.ActivityWelcomeBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.LoginViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.loginViewModel = loginViewModel

        viewModelCallback()

    }

    fun viewModelCallback() {
        with(loginViewModel) {
            goToMain.observe(this@LoginActivity, Observer {
                goToMainActivity()
            })
            goToSignUp.observe(this@LoginActivity, Observer {
                goToSignUpActivity()
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
}