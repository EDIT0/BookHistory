package com.ejstudio.bookhistory.presentation.view.fragment.main

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.FragmentBookListBinding
import com.ejstudio.bookhistory.databinding.FragmentSettingBinding
import com.ejstudio.bookhistory.presentation.view.activity.login.LoginActivity
import com.ejstudio.bookhistory.presentation.view.activity.main.MainActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import com.ejstudio.bookhistory.util.UserInfo
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : Fragment() {

    lateinit var binding: FragmentSettingBinding
//    private val mainViewModel: MainViewModel by viewModel()
    lateinit var mainViewModel: MainViewModel

    lateinit var dialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        mainViewModel = (activity as MainActivity).mainViewModel
        binding.mainViewModel = mainViewModel

        viewModelCallback()
        buttonClickListener()

        return binding.root
    }

    fun viewModelCallback() {
        with(mainViewModel) {
            logoutSuccess.observe(viewLifecycleOwner, Observer {
                goToLoginActivity()
            })
            logoutFail.observe(viewLifecycleOwner, Observer {
                Toast.makeText(binding.root.context, "로그아웃 실패", Toast.LENGTH_SHORT).show()
            })
        }
    }

    fun buttonClickListener() {
        binding.tvLogout.setOnClickListener {
            dialog = Dialog(binding.root.context);       // Dialog 초기화
//                deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
            dialog.setContentView(R.layout.dialog_delete_idx_book_info);
            val title = ""
            val subTitle = "로그아웃 하시겠습니까?"
            showDeleteDialog(title, subTitle)
        }
    }

    fun showDeleteDialog(title: String, subTitle: String) {
        dialog.findViewById<TextView>(R.id.dialog_tv_title).setText(title)
        dialog.findViewById<TextView>(R.id.dialog_tv_subTitle).setText(subTitle)

        dialog.show()

        val dialog_cancel: Button = dialog.findViewById(R.id.dialog_cancel)
        dialog_cancel.setOnClickListener {
            dialog.dismiss()
        }

        val dialog_confirmation: Button = dialog.findViewById(R.id.dialog_confirmation)
        dialog_confirmation.setOnClickListener {
            mainViewModel.accountLogout()
            dialog.dismiss()
        }
    }

    fun goToLoginActivity() {
        UserInfo.email = ""
        var goToLoginActivity = Intent(binding.root.context, LoginActivity::class.java)
        goToLoginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        goToLoginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(goToLoginActivity)
    }
}