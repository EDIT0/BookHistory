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