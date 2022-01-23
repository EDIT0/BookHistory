package com.ejstudio.bookhistory.presentation.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityFindPassword2Binding
import com.ejstudio.bookhistory.databinding.ActivityFindPasswordBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.FindPassword2ViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.FindPasswordViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FindPassword2Activity : BaseActivity<ActivityFindPassword2Binding>(R.layout.activity_find_password2) {

    private val TAG = FindPassword2Activity::class.java.simpleName
    private val findPassword2ViewModel: FindPassword2ViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.findPassword2ViewModel = findPassword2ViewModel

        viewModelCallback()


    }

    fun viewModelCallback() {
        with(findPassword2ViewModel) {
            goToLogin.observe(this@FindPassword2Activity, Observer {
                Log.i(TAG, "호출")
                onBackPressed()
            })
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.not_move_activity, R.anim.rightout_activity)
    }
}