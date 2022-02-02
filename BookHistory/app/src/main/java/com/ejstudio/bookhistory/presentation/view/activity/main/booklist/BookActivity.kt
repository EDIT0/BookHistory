package com.ejstudio.bookhistory.presentation.view.activity.main.booklist


import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityBookBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.adapter.main.booklist.BookMemoViewPagerFragmentAdapter
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.BookViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

import com.google.android.material.tabs.TabLayoutMediator
import android.widget.Button
import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookInfoBottomSheetDialogFragment

import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookListMenuSelectionBottomSheetDialogFragment


class BookActivity : BaseActivity<ActivityBookBinding>(R.layout.activity_book) {

    private val TAG: String? = BookActivity::class.java.simpleName
    public val bookViewModel: BookViewModel by viewModel()
    private lateinit var bookMemoViewPagerFragmentAdapter: BookMemoViewPagerFragmentAdapter
    lateinit var deleteDialog: Dialog
    private lateinit var bottomSheet: BookInfoBottomSheetDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.bookViewModel = bookViewModel

        recvIntent()
        viewModelCallback()
        viewPagerAndTabLayoutSetting()
    }

    fun recvIntent() {
        bookViewModel.book_idx = intent.getIntExtra("idx", 0)
        bookViewModel.reading_state = intent.extras?.getString("reading_state")!!
        Log.i(TAG, "책 idx: " + bookViewModel.book_idx + " 책 메뉴상태: " + bookViewModel.reading_state)
        bookViewModel.getIdxBookInfo()
    }

    fun viewModelCallback() {
        with(bookViewModel) {
            backButton.observe(this@BookActivity, Observer {
                activityBackButton()
            })
            bookInfo.observe(this@BookActivity, Observer {
                if(it != null) {
                    Log.i(TAG, "현재 선택한 책: " + it.title + " ${it.thumbnail}")
                    bookThumbnail = it.thumbnail
                    bookTitle.value = it.title
                    bookAuthors.value = it.authors
                    Glide.with(binding.root.context)
                        .load(it.thumbnail)
                        .error(R.drawable.img_bookcover_null)
                        .into(binding.bookInfoBackgroundView)
                    bookPublisher = it.publisher
                    bookDatetime = it.datetime
                    bookContents = it.contents
                    bookUrl = it.url
                }
            })
            deleteBook.observe(this@BookActivity, Observer {
                deleteDialog = Dialog(binding.root.context);       // Dialog 초기화
//                deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
                deleteDialog.setContentView(R.layout.dialog_delete_idx_book_info);
                showDeleteDialog()
            })
            showBookInfo.observe(this@BookActivity, Observer {
                bottomSheet = BookInfoBottomSheetDialogFragment()
//                val bundle = Bundle()
//                bundle.putString("state", mainViewModel._selectedMenu.value.toString())
//                bottomSheet.setArguments(bundle)
                bottomSheet.show(supportFragmentManager, "tag")
            })
            contentsSeeDetail.observe(this@BookActivity, Observer {
                goToContentsSeeDetail()
            })
        }
    }

    fun viewPagerAndTabLayoutSetting() {
        // 2) FragmentStateAdapter 생성 : Fragment 여러개를 ViewPager2에 연결해주는 역할
        bookMemoViewPagerFragmentAdapter = BookMemoViewPagerFragmentAdapter(this)

        // 3) ViewPager2의 adapter에 설정
        binding.viewPager2Container.adapter = bookMemoViewPagerFragmentAdapter

        // ###### TabLayout과 ViewPager2를 연결
        // 2. TabLayout과 ViewPager2를 연결하고, TabItem의 메뉴명을 설정한다.
        TabLayoutMediator(binding.tabLayout, binding.viewPager2Container, {tab, position ->
            if(position == 0) {
                tab.setIcon(R.drawable.ic_notes_black_24dp)
            }
            else if(position == 1) {
                tab.setIcon(R.drawable.ic_image_24dp)
            }

        }).attach()
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
            bookViewModel.deleteIdxBookInfo()
            deleteDialog.dismiss()
        }
    }

    fun goToContentsSeeDetail() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(bookViewModel.bookUrl))
        startActivity(browserIntent)
    }
}