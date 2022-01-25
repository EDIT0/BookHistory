package com.ejstudio.bookhistory.presentation.view.activity.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivitySignUp2Binding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.activity.main.MainActivity
import com.ejstudio.bookhistory.presentation.view.fragment.login.ToSBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.viewmodel.login.SignUp2ViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUp2Activity : BaseActivity<ActivitySignUp2Binding>(R.layout.activity_sign_up2) {

    private val signUp2ViewModel: SignUp2ViewModel by viewModel()
    private lateinit var manager: InputMethodManager
    private lateinit var bottomSheet: ToSBottomSheetDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.signUp2ViewModel = signUp2ViewModel


        revIntent()
        keyboardSetting()
        viewModelCallback()
        textWatcher()
    }

    fun revIntent() {
        var intent = getIntent()
        signUp2ViewModel.email = intent.getStringExtra("email")?:""
    }

    fun keyboardSetting() {
        manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        binding.etInputPassword.requestFocus()

    }

    fun viewModelCallback() {
        with(signUp2ViewModel) {
            requestSnackbar.observe(this@SignUp2Activity, Observer {
                showSnackbar(signUp2ViewModel.snackbarMessage)
            })
            requestSnackbarAction.observe(this@SignUp2Activity, Observer {
                showSnackbarAction(signUp2ViewModel.snackbarMessage)
            })
            goToMain.observe(this@SignUp2Activity, Observer {
                goToMainActivity()
            })
            showExpandableToS.observe(this@SignUp2Activity, Observer {
                bottomSheet = ToSBottomSheetDialogFragment()
                bottomSheet.show(supportFragmentManager, "tag")
            })
        }
    }

    fun textWatcher() {
        binding.etInputPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if(signUp2ViewModel.password.value == null || !(signUp2ViewModel.password.value.toString().length >= 6 && signUp2ViewModel.password.value.toString().length <= 16)) {
                    binding.textInputLayoutInputPassword.error = "패스워드는 6~16자 이내로 설정해주세요"
                    binding.btnCreateUser.isEnabled = false
                } else {
                    if(signUp2ViewModel.password.value.toString() == signUp2ViewModel.checkPassword.value.toString()) {
                        binding.textInputLayoutInputCheckPassword.helperText = "확인"
                        binding.btnCreateUser.isEnabled = true
                        binding.btnCreateUser.requestFocus()
                        manager.hideSoftInputFromWindow(getCurrentFocus()?.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
                    } else {
                        binding.textInputLayoutInputCheckPassword.error = "비밀번호를 확인해주세요."
                        binding.btnCreateUser.isEnabled = false
                    }
                    binding.textInputLayoutInputPassword.helperText = "사용 가능한 비밀번호입니다."
                }
            }

        })
        binding.etInputCheckPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                with(signUp2ViewModel) {
                    if(password.value.toString() == checkPassword.value.toString()) {
                        if(password.value == null || !(password.value.toString().length >= 6 && password.value.toString().length <= 16)) {
                            binding.textInputLayoutInputCheckPassword.error = "비밀번호를 확인해주세요."
                            binding.btnCreateUser.isEnabled = false
                        } else {
                            binding.textInputLayoutInputCheckPassword.helperText = "확인"
                            binding.btnCreateUser.isEnabled = true
                            binding.btnCreateUser.requestFocus()
                            manager.hideSoftInputFromWindow(getCurrentFocus()?.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
                        }
                    } else {
                        binding.textInputLayoutInputCheckPassword.error = "비밀번호를 확인해주세요."
                        binding.btnCreateUser.isEnabled = false
                    }
                }
            }

        })
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        overridePendingTransition(R.anim.rightin_activity, R.anim.leftout_activity)
    }
}