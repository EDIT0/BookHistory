package com.ejstudio.bookhistory.presentation.view.fragment.main

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import com.ejstudio.bookhistory.databinding.FragmentSettingBinding
import com.ejstudio.bookhistory.presentation.view.activity.login.LoginActivity
import com.ejstudio.bookhistory.presentation.view.activity.main.MainActivity
import com.ejstudio.bookhistory.presentation.view.fragment.login.ToSBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import com.ejstudio.bookhistory.util.UserInfo
import com.kakao.sdk.user.UserApiClient




class SettingFragment : Fragment() {

    companion object {
        const val LOGOUT = "LOGOUT"
        const val CHANGE_PASSWORD = "CHANGE_PASSWORD"
        const val REMOVE_ACCOUNT = "REMOVE_ACCOUNT"
    }



    lateinit var binding: FragmentSettingBinding
//    private val mainViewModel: MainViewModel by viewModel()
    lateinit var mainViewModel: MainViewModel

    lateinit var dialog: Dialog

    private lateinit var bottomSheet: ToSBottomSheetDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        mainViewModel = (activity as MainActivity).mainViewModel
        binding.mainViewModel = mainViewModel


        settingUserIdAndVersionName()
        settingButton()
        viewModelCallback()
        buttonClickListener()

        return binding.root
    }

    fun settingUserIdAndVersionName() {
        if(!UserInfo.email.contains("@")) {
//            binding.tvUserId.text = "????????? ?????????(${UserInfo.email.substring(0,3)}*****)"
            binding.tvUserId.text = "????????? ?????????(${UserInfo.email})"
        } else {
            binding.tvUserId.text = UserInfo.email
        }
        binding.tvVersionName.text = getVersion(requireActivity())
    }

    fun getVersion(context: Context): String? {
        var versionName = ""
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            versionName = pInfo.versionName + ""
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
    }

    fun settingButton() {
        if(!UserInfo.email.contains("@")) {
            binding.tvChangePW.visibility = View.GONE
        }
    }

    fun viewModelCallback() {
        with(mainViewModel) {
            logoutSuccess.observe(viewLifecycleOwner, Observer {
                goToLoginActivity()
            })
            logoutFail.observe(viewLifecycleOwner, Observer {
                Toast.makeText(binding.root.context, "???????????? ??????", Toast.LENGTH_SHORT).show()
            })
        }
    }

    fun buttonClickListener() {
        binding.tvChangePW.setOnClickListener {
            dialog = Dialog(binding.root.context)
            dialog.setContentView(R.layout.dialog_delete_idx_book_info)
            val title = "???????????? ??????????????????"
            val subTitle = "????????? ?????? ??????????????? ???????????????\n????????? ????????? ?????? ???????????? ?????????"
            showDeleteDialog(title, subTitle, CHANGE_PASSWORD)
        }
        binding.tvLogout.setOnClickListener {
            dialog = Dialog(binding.root.context)       // Dialog ?????????
//                deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // ????????? ??????
            dialog.setContentView(R.layout.dialog_delete_idx_book_info)
            val title = ""
            val subTitle = "???????????? ???????????????????"
            showDeleteDialog(title, subTitle, LOGOUT)
        }
        binding.tvRemoveAccount.setOnClickListener {
            dialog = Dialog(binding.root.context)
            dialog.setContentView(R.layout.dialog_delete_idx_book_info)
            val title = "???????????? ???????????????????"
            val subTitle = "?????? ????????? ?????? ???????????????"
            showDeleteDialog(title, subTitle, REMOVE_ACCOUNT)
        }
        binding.tvTos.setOnClickListener {
            bottomSheet = ToSBottomSheetDialogFragment()
            bottomSheet.show(requireActivity().supportFragmentManager, "tag")
        }
    }

    fun showDeleteDialog(title: String, subTitle: String, type: String) {
        dialog.findViewById<TextView>(R.id.dialog_tv_title).setText(title)
        dialog.findViewById<TextView>(R.id.dialog_tv_subTitle).setText(subTitle)

        if(title.length == 0) {
            dialog.findViewById<TextView>(R.id.dialog_tv_title).visibility = View.GONE
        } else {
            dialog.findViewById<TextView>(R.id.dialog_tv_title).visibility = View.VISIBLE
        }

        if(subTitle.length == 0) {
            dialog.findViewById<TextView>(R.id.dialog_tv_subTitle).visibility = View.GONE
        } else {
            dialog.findViewById<TextView>(R.id.dialog_tv_subTitle).visibility = View.VISIBLE
        }

        dialog.show()

        val dialog_cancel: Button = dialog.findViewById(R.id.dialog_cancel)
        val dialog_confirmation: Button = dialog.findViewById(R.id.dialog_confirmation)

        when(type) {
            CHANGE_PASSWORD -> {
                dialog_confirmation.setText("??????")
            }
            LOGOUT -> {
                dialog_confirmation.setText("????????????")
            }
            REMOVE_ACCOUNT -> {
                dialog_confirmation.setText("????????????")
            }
        }


        dialog_cancel.setOnClickListener {
            dialog.dismiss()
        }


        dialog_confirmation.setOnClickListener {
//            if(dialog_confirmation.text.equals("????????????")) {
//                if(UserApiClient.instance != null) {
//                    UserApiClient.instance.unlink { throwable: Throwable? ->
//                        if (throwable != null) {
//                            // @brief : ?????? ?????? ??????
//                            Log.e("[?????????] ????????????", "?????? ?????? ??????")
//                        } else {
//                            // @brief : ?????? ?????? ??????
//                            Log.i("kakaoLogout", "?????? ?????? ??????. SDK?????? ?????? ??????")
//                        }
//                    }
//                }
//            }
            mainViewModel.accountLogout(type)
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