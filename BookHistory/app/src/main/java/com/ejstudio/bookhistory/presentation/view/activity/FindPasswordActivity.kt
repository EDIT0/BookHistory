package com.ejstudio.bookhistory.presentation.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityFindPasswordBinding
import com.ejstudio.bookhistory.databinding.ActivityLoginBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.FindPasswordViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.SignUpViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FindPasswordActivity : BaseActivity<ActivityFindPasswordBinding>(R.layout.activity_find_password) {

    private val findPasswordViewModel: FindPasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.findPasswordViewModel = findPasswordViewModel

        viewModelCallback()
        textWatcher()


    }

    fun viewModelCallback() {
        with(findPasswordViewModel) {
            goToFindPassword2.observe(this@FindPasswordActivity, Observer {
                goToFindPassword2()
            })
            goToLogin.observe(this@FindPasswordActivity, Observer {
                finish()
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
    }

    fun goToFindPassword2() {


//        startActivity(Intent(this, FindPassword2Activity::class.java))
    }
}