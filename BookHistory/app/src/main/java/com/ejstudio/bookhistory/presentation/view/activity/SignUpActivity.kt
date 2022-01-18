package com.ejstudio.bookhistory.presentation.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivitySignUpBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.SignUpViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {

    private val signUpViewModel: SignUpViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.signUpViewModel = signUpViewModel

        viewModelCallback()
        textWatcher()
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
            })
//            // 이메일 인증 성공
//            successCheckNumber.observe(this@SignUpActivity, Observer {
//                binding.etInputEmail.isClickable =false
//                binding.etInputEmail.isFocusable = false
//                binding.btnSendNumber.isClickable = false
//                binding.btnSendNumber.isFocusable = false
//                showSnackbar(signUpViewModel.snackbarMessage)
//            })
            number.observe(this@SignUpActivity, Observer {
                if(randomNum.toString() == number.value.toString()) {
//                    snackbarMessage = "인증되었습니다"
//                    _successCheckNumber.value = Unit
//                    passEmail = true
                } else {
//                    snackbarMessage = "인증번호를 다시 확인해주세요"
//                    _requestToast.value = Unit
                }
            })
            // 로그인 성공
//            successLogin.observe(this@SignUpActivity, Observer {
//                goToMainActivity()
//                finish()
//            })
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
                }
            }

        })
    }

    private fun goToLoginActivity() {
        finish()
    }

    private fun goToSignUp2Activity() {
        val intent = Intent(this, SignUp2Activity::class.java)
        intent.putExtra("email", signUpViewModel.authEmail)
        startActivity(intent)
        finish()
    }

//    private fun goToMainActivity() {
//        startActivity(Intent(this, MainActivity::class.java))
//        finish()
//    }
}