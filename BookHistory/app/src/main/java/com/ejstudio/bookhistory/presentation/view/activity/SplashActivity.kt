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
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.kakao.sdk.common.KakaoSdk.keyHash
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.gms.tasks.OnCompleteListener

import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import android.app.Activity
import android.app.AlertDialog

import android.content.SharedPreferences

import android.content.DialogInterface

import android.content.pm.PackageInfo
import android.net.Uri
import com.ejstudio.bookhistory.BuildConfig
import com.ejstudio.bookhistory.data.api.ApiClient
import java.lang.Exception


class SplashActivity : AppCompatActivity() {

    private val TAG = SplashActivity::class.java.simpleName
    lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
    private val splashViewModel: SplashViewModel by viewModel()

    private lateinit var mActivity: SplashActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivity = this

        remoteConfig()

//        initViewModelCallback()
//        Log.i(TAG, keyHash)
//        splashViewModel.doSplash()
    }

    private fun remoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()

        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.default_remote_config)

        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d(TAG, "Config params updated: $updated")
                } else {
                }
                displayWelcomeMessage()
            }
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

    private fun displayWelcomeMessage() {
        var versionCode = 99999999

        /* 버전코드 가져오기*/
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            versionCode = packageInfo.versionCode
        } catch (e: Exception) {
        }
        val server_check = mFirebaseRemoteConfig.getBoolean("server_check")
        val server_message = mFirebaseRemoteConfig.getString("server_message")
        val version_code_number = mFirebaseRemoteConfig.getLong("version_code_number")
        val version_code_message = mFirebaseRemoteConfig.getString("version_code_message")
        if (server_check) {
//            val builder: AlertDialog.Builder = AlertDialog.Builder(mActivity)
//            builder
//                .setMessage(server_message)
//                .setCancelable(false)
//                .setPositiveButton("확인",
//                    DialogInterface.OnClickListener { dialogInterface, i -> finish() })
//            builder.create().show()
            Toast.makeText(mActivity, server_message, Toast.LENGTH_LONG).show()
            finish()
        } else if (version_code_number > versionCode) { // 앱 업데이트 필요(강제) 할 때
//            val builder: AlertDialog.Builder = AlertDialog.Builder(mActivity)
//            builder
//                .setMessage(version_code_message)
//                .setCancelable(false)
//                .setPositiveButton("확인",
//                    DialogInterface.OnClickListener { dialogInterface, i ->
//                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ApiClient.GOOGLE_PLAYSTORE_APP_PAGE))
//                        startActivity(intent)
//                        finish()
//                    })
//            builder.create().show()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.GOOGLE_PLAYSTORE_APP_PAGE))
            startActivity(intent)
            Toast.makeText(mActivity, version_code_message, Toast.LENGTH_LONG).show()
            finish()
        } else {
            initViewModelCallback()
            Log.i(TAG, keyHash)
            splashViewModel.doSplash()
        }
    }
}