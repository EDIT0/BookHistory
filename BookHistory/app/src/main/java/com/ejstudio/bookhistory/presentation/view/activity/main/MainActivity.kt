package com.ejstudio.bookhistory.presentation.view.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.FragmentManager
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityLoginBinding
import com.ejstudio.bookhistory.databinding.ActivityMainBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.activity.SplashActivity
import com.ejstudio.bookhistory.presentation.view.activity.login.LoginActivity
import com.ejstudio.bookhistory.presentation.view.fragment.main.BookListFragment
import com.ejstudio.bookhistory.presentation.view.fragment.main.BookSearchFragment
import com.ejstudio.bookhistory.presentation.view.fragment.main.MyBookHistoryFragment
import com.ejstudio.bookhistory.presentation.view.fragment.main.SettingFragment
import com.ejstudio.bookhistory.presentation.view.viewmodel.login.LoginViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import com.ejstudio.bookhistory.util.UserInfo
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main), KoinComponent {

    companion object {
        const val NORMAL_LOGIN = "NORMAL_LOGIN"
        const val AUTO_LOGIN = "AUTO_LOGIN"
    }

    private val TAG: String? = MainActivity::class.java.simpleName
    public val mainViewModel: MainViewModel by viewModel()

//    lateinit var bookListFragment: BookListFragment
//    lateinit var bookSearchFragment: BookSearchFragment
//    lateinit var myBookHistoryFragment: MyBookHistoryFragment
//    lateinit var settingFragment: SettingFragment

    val bookListFragment: BookListFragment by inject()
    val bookSearchFragment: BookSearchFragment by inject()
    val myBookHistoryFragment: MyBookHistoryFragment by inject()
    val settingFragment: SettingFragment by inject()

    lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding.mainViewModel = mainViewModel
        Log.i(TAG, "코드0: ${mainViewModel.hashCode()}")

        Log.i(TAG, "로그인 계정: " + UserInfo.email)

        /*
        * 중복 로그인이 불가하므로 로그인 시 (자동로그인 제외) 서버 중복로그인방지토큰을 업데이트 한다. 또한, 내부저장소에 중복로그인방지토큰을 저장한다.
        * 그 후
        * 1. 자동로그인 일 경우
        * 서버에서 가져온 토큰과 현재 내부 토큰을 비교하여 같으면 로그인을 유지시키고, 다르면 로그아웃 시킨다.
        * 2. 일반 로그인 or 카카오로그인으로 들어온 경우
        * 서버에서 가져온 토큰과 현재 내부 토큰을 비교하여 (토큰은 있을 수도 있고 없을 수도 있음) 같으면 로그인시키고,
        * 다르면 방금 로그인한 유저의 내부 DB를 초기화하고, 서버에서 값을 가져와 내부 DB에 적용시킨다.
        * */
        var kindOfLogin = intent.getStringExtra("WhatIsTheTypeOfLogin")
        Log.i(TAG, "로그인 방식: $kindOfLogin")

        mainViewModel.getProtectDuplicateLoginToken() // 서버 토큰 가져오기

        if(kindOfLogin.equals(AUTO_LOGIN)) {
            // 자동로그인 일 경우

        } else if(kindOfLogin.equals(NORMAL_LOGIN)) {
            // 일반로그인 일 경우

        }

        /*
        * 하위 프래그먼트
        * */
//        bookListFragment = BookListFragment()
//        bookSearchFragment = BookSearchFragment()
//        myBookHistoryFragment = MyBookHistoryFragment()
//        settingFragment = SettingFragment()

        /*
        * 관리 매니저
        * */
        fragmentManager = getSupportFragmentManager();

        /*
        * 메인으로 보여줄 프래그먼트 불러오기
        * */
        fragmentManager.beginTransaction()
            .replace(R.id.mainFramelayout, bookListFragment)
            .commitAllowingStateLoss()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.tab_list -> {
                        fragmentManager.beginTransaction()
                            .replace(R.id.mainFramelayout, bookListFragment)
                            .commit()
                        true
                    }
                    R.id.tab_search -> {
                        fragmentManager.beginTransaction()
                            .replace(R.id.mainFramelayout, bookSearchFragment)
                            .commit()
                        true
                    }
                    R.id.tab_history -> {
                        fragmentManager.beginTransaction()
                            .replace(R.id.mainFramelayout, myBookHistoryFragment)
                            .commit()
                        true
                    }
                    R.id.tab_setting -> {
                        fragmentManager.beginTransaction()
                            .replace(R.id.mainFramelayout, settingFragment)
                            .commit()
                        true
                    }
                }
                return true
            }

        })
    }
}