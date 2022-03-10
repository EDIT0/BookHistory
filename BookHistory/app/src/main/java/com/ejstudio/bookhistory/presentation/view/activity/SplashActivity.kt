package com.ejstudio.bookhistory.presentation.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.presentation.view.activity.login.LoginActivity
import com.ejstudio.bookhistory.presentation.view.activity.main.MainActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.SplashViewModel
import com.kakao.sdk.common.KakaoSdk.keyHash
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val TAG = SplashActivity::class.java.simpleName
    private val splashViewModel: SplashViewModel by viewModel()

    private lateinit var mActivity: SplashActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivity = this

        initViewModelCallback()

        Log.i(TAG, keyHash)
        splashViewModel.doSplash()
    }

    private fun initViewModelCallback() {
        with(splashViewModel) {
            goToWelcome.observe(this@SplashActivity, Observer {
                goToWelcomeActivity()
            })
            goToLogin.observe(this@SplashActivity, Observer {
                goToLoginActivity()
            })
            goToMain.observe(this@SplashActivity, Observer {
                goToMainActivity()
            })
            requestToast.observe(this@SplashActivity, Observer {
                Toast.makeText(mActivity, getString(R.string.NETWORK_NOT_CONNECTED), Toast.LENGTH_SHORT).show()
                Thread(Runnable {
                    Thread.sleep(2000)
                    finish()
                }).start()

            })
        }
    }

    fun goToWelcomeActivity() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }

    fun goToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    fun goToMainActivity() {
        var intent = Intent(this, MainActivity::class.java)
        intent.putExtra("WhatIsTheTypeOfLogin", MainActivity.AUTO_LOGIN)
        startActivity(intent)
        finish()
    }
}