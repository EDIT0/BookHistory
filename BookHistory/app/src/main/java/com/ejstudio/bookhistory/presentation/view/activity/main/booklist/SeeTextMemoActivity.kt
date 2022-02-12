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

class SeeTextMemoActivity : BaseActivity<ActivitySeeTextMemoBinding>(R.layout.activity_see_text_memo) {

    private val TAG: String? = SeeTextMemoActivity::class.java.simpleName
    public val seeTextMemoViewModel: SeeTextMemoViewModel by viewModel()
    lateinit var deleteDialog: Dialog
    private lateinit var manager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.seeTextMemoViewModel = seeTextMemoViewModel

        keyBoardSetting()
        recvIntent()
        viewModelCallback()
    }

    fun keyBoardSetting() {
        manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    fun recvIntent() {
        seeTextMemoViewModel.textMemoIdx = intent.getIntExtra("textMemoIdx", 0)
        seeTextMemoViewModel.bookTitle = intent.getStringExtra("bookTitle")!!
        Log.i(TAG, "책 idx: " + seeTextMemoViewModel.textMemoIdx)
    }

    fun viewModelCallback() {
        with(seeTextMemoViewModel) {
            backButton.observe(this@SeeTextMemoActivity, Observer {
                if(isEditMode.value.toString().toBoolean()) {
                    Log.i(TAG, "텍스트 길이 ${memo_contents.value.toString().length}")
                    textSynchronization()
                    isEditMode.value = false
                    manager.hideSoftInputFromWindow(getCurrentFocus()?.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
                } else {
                    activityBackButton()
                }
            })
            textMemo.observe(this@SeeTextMemoActivity, Observer {
                if(it != null) {
                    Log.i(TAG, "결과: ${it.idx} ${it.booklist_idx} ${it.memo_contents} ${it.save_datetime}")

                    memo_contents.value = it.memo_contents
                    textSynchronization()
                }
            })
            deleteTextMemo.observe(this@SeeTextMemoActivity, Observer {
                deleteDialog = Dialog(binding.root.context);       // Dialog 초기화
//                deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
                deleteDialog.setContentView(R.layout.dialog_delete_idx_book_info);
                deleteDialog.findViewById<TextView>(R.id.dialog_tv_subTitle).setText("글을 삭제하시겠습니까?")
                deleteDialog.findViewById<TextView>(R.id.dialog_tv_title).visibility = View.INVISIBLE
                showDeleteDialog()
            })
            editButton.observe(this@SeeTextMemoActivity, Observer {
                isEditMode.value = true
                binding.etContents.requestFocus()
                Log.i(TAG, "텍스트 길이 ${binding.etContents.text.length}")
                binding.etContents.setSelection(binding.etContents.text.length)
                manager.showSoftInput(binding.etContents, InputMethodManager.SHOW_IMPLICIT);
            })
        }
    }

    fun activityBackButton() {
        onBackPressed()
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
}