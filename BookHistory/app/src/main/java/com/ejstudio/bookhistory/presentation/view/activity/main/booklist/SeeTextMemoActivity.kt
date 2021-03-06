package com.ejstudio.bookhistory.presentation.view.activity.main.booklist

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityBookBinding
import com.ejstudio.bookhistory.databinding.ActivitySeeTextMemoBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookInfoBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookMenuChangeBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.BookViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.SeeTextMemoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception
import android.widget.Toast

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import com.ejstudio.bookhistory.presentation.view.viewmodel.login.FindPasswordViewModel


class SeeTextMemoActivity : BaseActivity<ActivitySeeTextMemoBinding>(R.layout.activity_see_text_memo) {

    private val TAG: String? = SeeTextMemoActivity::class.java.simpleName
    public val seeTextMemoViewModel: SeeTextMemoViewModel by viewModel()
    lateinit var deleteDialog: Dialog
    lateinit var dialog: Dialog
    private lateinit var manager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.seeTextMemoViewModel = seeTextMemoViewModel

        keyBoardSetting()
        recvIntent()
        scrollingSetting()
        viewModelCallback()
        buttonClickListener()
    }

    fun keyBoardSetting() {
        manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    fun recvIntent() {
        seeTextMemoViewModel.textMemoIdx = intent.getIntExtra("textMemoIdx", 0)
        seeTextMemoViewModel.bookTitle = intent.getStringExtra("bookTitle")!!
        Log.i(TAG, "??? idx: " + seeTextMemoViewModel.textMemoIdx)
    }

    fun scrollingSetting() {
        binding.etContents.setOnScrollChangeListener { view, i, i2, i3, i4 ->
            binding.actionBarView.isSelected = binding.etContents.canScrollVertically(-1)
        }
    }

    fun viewModelCallback() {
        with(seeTextMemoViewModel) {
            backButton.observe(this@SeeTextMemoActivity, Observer {
                if(completed_save_memo) {
                    // ???????????? ?????? ????????? ???????????? ??????????????? ????????? ??????
                    super.onBackPressed()
                    overridePendingTransition(R.anim.not_move_activity, R.anim.fade_out)
                } else {
                    onBackPressed()
                }
//                if(isEditMode.value.toString().toBoolean()) {
//                    Log.i(TAG, "????????? ?????? ${memo_contents.value.toString().length}")
//                    textSynchronization()
//                    isEditMode.value = false
//                    manager.hideSoftInputFromWindow(getCurrentFocus()?.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
//                } else {
//                    activityBackButton()
//                }
            })
            textMemo.observe(this@SeeTextMemoActivity, Observer {
                if(it != null) {
                    Log.i(TAG, "??????: ${it.idx} ${it.booklist_idx} ${it.memo_contents} ${it.save_datetime}")

                    memo_contents.value = it.memo_contents
                    textSynchronization()
                }
            })
            deleteTextMemo.observe(this@SeeTextMemoActivity, Observer {
                deleteDialog = Dialog(binding.root.context);       // Dialog ?????????
//                deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // ????????? ??????
                deleteDialog.setContentView(R.layout.dialog_delete_idx_book_info)

                val title = ""
                val subTitle = "?????? ?????????????????????????"

                deleteDialog.findViewById<TextView>(R.id.dialog_tv_subTitle).setText(subTitle)
                deleteDialog.findViewById<TextView>(R.id.dialog_tv_title).setText(title)

                if(title.length == 0) {
                    deleteDialog.findViewById<TextView>(R.id.dialog_tv_title).visibility = View.GONE
                } else {
                    deleteDialog.findViewById<TextView>(R.id.dialog_tv_title).visibility = View.VISIBLE
                }

                if(subTitle.length == 0) {
                    deleteDialog.findViewById<TextView>(R.id.dialog_tv_subTitle).visibility = View.GONE
                } else {
                    deleteDialog.findViewById<TextView>(R.id.dialog_tv_subTitle).visibility = View.VISIBLE
                }

                showDeleteDialog()
            })
            editButton.observe(this@SeeTextMemoActivity, Observer {
                isEditMode.value = true
                binding.etContents.requestFocus()
                Log.i(TAG, "????????? ?????? ${binding.etContents.text.length}")
                binding.etContents.setSelection(binding.etContents.text.length)
                manager.showSoftInput(binding.etContents, InputMethodManager.SHOW_IMPLICIT);
            })
            requestSnackbar.observe(this@SeeTextMemoActivity, Observer {
                when(snackbarMessage) {
                    SeeTextMemoViewModel.MessageSet.NETWORK_NOT_CONNECTED.toString() -> {
                        snackbarMessage = getString(R.string.NETWORK_NOT_CONNECTED)
                    }
                }
                showSnackbar(snackbarMessage)
            })
        }
    }

    fun activityBackButton() {
        onBackPressed()
    }

    override fun onBackPressed() {
        if(seeTextMemoViewModel.isEditMode.value.toString().toBoolean()) {
            // ?????????????????? ????????? ?????? ???
            dialog = Dialog(binding.root.context);       // Dialog ?????????
//                deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // ????????? ??????
            dialog.setContentView(R.layout.dialog_delete_idx_book_info);
            dialog.findViewById<TextView>(R.id.dialog_tv_subTitle).setText("??????????????? ??????????????????????")
            dialog.findViewById<TextView>(R.id.dialog_tv_title).setText("???????????? ?????? ????????? ????????????.")
            showDialog()

        } else {
            // ?????? ?????? ???????????? ????????? ?????? ???
            super.onBackPressed()
            overridePendingTransition(R.anim.not_move_activity, R.anim.fade_out)
        }
    }

    fun showDeleteDialog() {
        deleteDialog.show()

        val dialog_cancel: Button = deleteDialog.findViewById(R.id.dialog_cancel)
        dialog_cancel.setOnClickListener {
            deleteDialog.dismiss()
        }

        val dialog_confirmation: Button = deleteDialog.findViewById(R.id.dialog_confirmation)
        dialog_confirmation.setOnClickListener {
            seeTextMemoViewModel.deleteIdxTextMemo()
            deleteDialog.dismiss()
        }
    }

    fun showDialog() {
        dialog.show()

        val dialog_cancel: Button = dialog.findViewById(R.id.dialog_cancel)
        dialog_cancel.setOnClickListener {
            dialog.dismiss()
        }

        val dialog_confirmation: Button = dialog.findViewById(R.id.dialog_confirmation)
        dialog_confirmation.setOnClickListener {
            // ???????????? ??????
            binding.actionBarView.isSelected = false // ?????? ??????????????? false
            Log.i(TAG, "????????? ?????? ${seeTextMemoViewModel.memo_contents.value.toString().length}")
            seeTextMemoViewModel.textSynchronization()
            seeTextMemoViewModel.isEditMode.value = false
            manager.hideSoftInputFromWindow(getCurrentFocus()?.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
            dialog.dismiss()
        }
    }

    fun buttonClickListener() {
        binding.ibTextCopy.setOnClickListener {
            val clipboardManager: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("TEXT", binding.etContents.getText().toString().trim()) //??????????????? ID?????? ???????????? id ?????? ???????????? ??????
            clipboardManager.setPrimaryClip(clipData)

            showToast("???")
        }

        binding.ibShareButton.setOnClickListener {
            if(!seeTextMemoViewModel.checkNetworkState()) return@setOnClickListener
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_TEXT, binding.etContents.getText().toString().trim())
            startActivity(Intent.createChooser(sharingIntent, "Share using text"))

//            val sharingIntent = Intent(Intent.ACTION_SEND)
//            sharingIntent.type = "text/plain" // ?????? text
//
//            sharingIntent.putExtra(Intent.EXTRA_TEXT, binding.etContents.getText().toString().trim())
//            sharingIntent.setPackage("com.instagram.android") // ?????? text
//
//            startActivity(sharingIntent)
        }
    }
}