package com.ejstudio.bookhistory.presentation.view.activity.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivitySignUpBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.login.SignUpViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {

    private val TAG = SignUpActivity::class.java.simpleName
    private val signUpViewModel: SignUpViewModel by viewModel()
    private lateinit var manager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.signUpViewModel = signUpViewModel

        keyboardSetting()
        viewModelCallback()
        textWatcher()
    }

    fun keyboardSetting() {
        manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.etInputEmail.requestFocus()
    }

    fun viewModelCallback() {
        with(signUpViewModel) {
            goToLogin.observe(this@SignUpActivity, Observer {
                goToLoginActivity()
            })
            goToSignUp2.observe(this@SignUpActivity, Observer {
                goToSignUp2Activity()
            })
            requestSnackbar.observe(this@SignUpActivity, Observer {
                showSnackbar(signUpViewModel.snackbarMessage)
                binding.etInputNumber.requestFocus()
            })
        }
    }

    fun textWatcher() {
        binding.etInputEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if(binding.etInputEmail.text!!.isEmpty()) {
                    binding.textInputLayoutInputEmail.error = "이메일을 입력해주세요."
                } else if(!binding.etInputEmail.text!!.contains("@") || !binding.etInputEmail.text!!.contains(".")) {
                    binding.textInputLayoutInputEmail.error = "잘못된 형식의 이메일 주소입니다."
                } else if(binding.etInputEmail.text!!.contains("@") && binding.etInputEmail.text!!.contains(".")) {
                    binding.textInputLayoutInputEmail.helperText = " "
                }
            }

        })
        binding.etInputNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if(!(signUpViewModel.randomNum.toString() == signUpViewModel.number.value.toString())) {
                    binding.textInputLayoutInputNumber.error = "인증번호를 확인해주세요."
                } else {
                    binding.textInputLayoutInputNumber.helperText = "인증되었습니다."
                    binding.etInputEmail.isClickable = false
                    binding.etInputEmail.isFocusable = false
                    binding.btnSendNumber.isClickable = false
                    binding.btnSendNumber.isFocusable = false
                    binding.etInputNumber.isClickable = false
                    binding.etInputNumber.isFocusable = false
                    binding.btnGoToPasswordSetting.isEnabled = true
                    binding.btnGoToPasswordSetting.requestFocus()
                    manager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
                }
            }

        })
    }

    private fun goToLoginActivity() {
        onBackPressed()
    }

    private fun goToSignUp2Activity() {
        val intent = Intent(this, SignUp2Activity::class.java)
        intent.putExtra("email", signUpViewModel.authEmail)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.rightin_activity, R.anim.leftout_activity)
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.not_move_activity, R.anim.rightout_activity)
    }
}