package com.ejstudio.bookhistory.presentation.view.activity.main.booklist

import android.app.Dialog
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
import com.ejstudio.bookhistory.databinding.ActivitySeeImageMemoBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.BookViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.SeeImageMemoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeeImageMemoActivity : BaseActivity<ActivitySeeImageMemoBinding>(R.layout.activity_see_image_memo) {

    private val TAG: String? = SeeImageMemoActivity::class.java.simpleName
    public val seeImageMemoViewModel: SeeImageMemoViewModel by viewModel()
    lateinit var deleteDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.seeImageMemoViewModel = seeImageMemoViewModel

        recvIntent()
        viewModelCallback()
        binding.imageView.setImageResource(R.drawable.img_bookcover_null);
    }

    fun recvIntent() {
        seeImageMemoViewModel.imageMemoIdx = intent.getIntExtra("imageMemoIdx", 0)
        seeImageMemoViewModel.imageUrl.value = intent.getStringExtra("imageUrl")
        seeImageMemoViewModel.bookTitle = intent.getStringExtra("bookTitle")!!
        Log.i(TAG, "책 idx: " + seeImageMemoViewModel.imageMemoIdx + " / " + seeImageMemoViewModel.imageUrl.value.toString())
    }

    fun viewModelCallback() {
        with(seeImageMemoViewModel) {
            backButton.observe(this@SeeImageMemoActivity, Observer {
                activityBackButton()
            })
            imageUrl.observe(this@SeeImageMemoActivity, Observer {
                Glide.with(binding.root)
                    .load(imageUrl.value.toString())
                    .into(binding.imageView)
            })
            deleteImageMemo.observe(this@SeeImageMemoActivity, Observer {
                deleteDialog = Dialog(binding.root.context);       // Dialog 초기화
//                deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
                deleteDialog.setContentView(R.layout.dialog_delete_idx_book_info);
                deleteDialog.findViewById<TextView>(R.id.dialog_tv_subTitle).setText("그림을 삭제하시겠습니까?")
                deleteDialog.findViewById<TextView>(R.id.dialog_tv_title).visibility = View.INVISIBLE
                showDeleteDialog()
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
            seeImageMemoViewModel.deleteIdxImageMemo()
            deleteDialog.dismiss()
        }
    }
}