package com.ejstudio.bookhistory.presentation.view.activity.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityFindPasswordBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.login.FindPasswordViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FindPasswordActivity : BaseActivity<ActivityFindPasswordBinding>(R.layout.activity_find_password) {

    private val TAG = FindPasswordActivity::class.java.simpleName
    private val findPasswordViewModel: FindPasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.findPasswordViewModel = findPasswordViewModel

        focusWidget()
        viewModelCallback()
        textWatcher()


    }

    fun focusWidget() {
        binding.etInputEmail.requestFocus()
    }

    fun viewModelCallback() {
        with(findPasswordViewModel) {
            goToFindPassword2.observe(this@FindPasswordActivity, Observer {
                goToFindPassword2Activity()
            })
            goToLogin.observe(this@FindPasswordActivity, Observer {
                onBackPressed()
            })
            requestSnackbar.observe(this@FindPasswordActivity, Observer {
                when(snackbarMessage) {
                    FindPasswordViewModel.MessageSet.NETWORK_NOT_CONNECTED.toString() -> {
                        snackbarMessage = getString(R.string.NETWORK_NOT_CONNECTED)
                    }
                    FindPasswordViewModel.MessageSet.RETRY.toString() -> {
                        snackbarMessage = getString(R.string.RETRY)
                    }
                    FindPasswordViewModel.MessageSet.NOT_USER.toString() -> {
                        snackbarMessage = getString(R.string.NOT_USER)
                    }
                }
                showSnackbar(snackbarMessage)
            })
            completeCheckEmail.observe(this@FindPasswordActivity, Observer {
                findPasswordViewModel.sendFindPasswordEmail()
            })
        }
    }

    fun textWatcher() {
        binding.etInputEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if(binding.etInputEmail.text!!.isEmpty()) {
                    binding.textInputLayoutInputEmail.error = getString(R.string.INPUT_YOUR_EMAIL)
                } else if(!binding.etInputEmail.text!!.contains("@") || !binding.etInputEmail.text!!.contains(".")) {
                    binding.textInputLayoutInputEmail.error = getString(R.string.THIS_IS_NOT_EMAILFORM)
                } else if(binding.etInputEmail.text!!.contains("@") && binding.etInputEmail.text!!.contains(".")) {
                    binding.textInputLayoutInputEmail.helperText = " "
                }
            }

        })
    }

    fun goToFindPassword2Activity() {
        startActivity(Intent(this, FindPassword2Activity::class.java))
        finish()
        overridePendingTransition(R.anim.leftout_activity, R.anim.rightin_activity)
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.not_move_activity, R.anim.rightout_activity)
    }
}