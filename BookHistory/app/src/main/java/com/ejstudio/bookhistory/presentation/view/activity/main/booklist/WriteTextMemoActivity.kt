package com.ejstudio.bookhistory.presentation.view.activity.main.booklist

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityBookBinding
import com.ejstudio.bookhistory.databinding.ActivityWriteTextMemoBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookInfoBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookMenuChangeBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.BookViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.WriteTextMemoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WriteTextMemoActivity : BaseActivity<ActivityWriteTextMemoBinding>(R.layout.activity_write_text_memo) {

    private val TAG: String? = WriteTextMemoActivity::class.java.simpleName
    public val writeTextMemoViewModel: WriteTextMemoViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.writeTextMemoViewModel = writeTextMemoViewModel

        recvIntent()
        viewModelCallback()
    }

    fun recvIntent() {
        writeTextMemoViewModel.book_idx = intent.getIntExtra("book_idx", 0)
        writeTextMemoViewModel.bookTitle = intent.getStringExtra("bookTitle")!!
        Log.i(TAG, "책 idx: " + writeTextMemoViewModel.book_idx)
    }

    fun viewModelCallback() {
        with(writeTextMemoViewModel) {
            backButton.observe(this@WriteTextMemoActivity, Observer {
                activityBackButton()
            })
            showToast.observe(this@WriteTextMemoActivity, Observer {
                showToast(toastMessage)
            })
        }
    }

    fun activityBackButton() {
        onBackPressed()
    }
}