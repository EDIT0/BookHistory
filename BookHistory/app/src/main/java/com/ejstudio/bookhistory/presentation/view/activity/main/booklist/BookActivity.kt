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
import android.widget.TextView
import com.ejstudio.bookhistory.presentation.view.activity.login.FindPassword2Activity
import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookInfoBottomSheetDialogFragment

import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookListMenuSelectionBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookMenuChangeBottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout


class BookActivity : BaseActivity<ActivityBookBinding>(R.layout.activity_book) {

    private val TAG: String? = BookActivity::class.java.simpleName
    public val bookViewModel: BookViewModel by viewModel()
    private lateinit var bookMemoViewPagerFragmentAdapter: BookMemoViewPagerFragmentAdapter
    lateinit var deleteDialog: Dialog
    private lateinit var bookInfoBottomSheetDialogFragment: BookInfoBottomSheetDialogFragment
    private lateinit var bookMenuChangeBottomSheetDialogFragment: BookMenuChangeBottomSheetDialogFragment

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
                    bookReadingState = it.reading_state
                    _selectedMenu.value = bookReadingState
                }
            })
            deleteBook.observe(this@BookActivity, Observer {
                deleteDialog = Dialog(binding.root.context);       // Dialog 초기화
//                deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
                deleteDialog.setContentView(R.layout.dialog_delete_idx_book_info);
                deleteDialog.findViewById<TextView>(R.id.dialog_tv_title).setText("책을 삭제하면\n글/그림 기록도 삭제됩니다.")
                deleteDialog.findViewById<TextView>(R.id.dialog_tv_subTitle).setText("지운 데이터는 복구가 불가능합니다.")
                showDeleteDialog()
            })
            showBookInfo.observe(this@BookActivity, Observer {
                bookInfoBottomSheetDialogFragment = BookInfoBottomSheetDialogFragment()
                bookInfoBottomSheetDialogFragment.show(supportFragmentManager, "tag")
            })
            contentsSeeDetail.observe(this@BookActivity, Observer {
                goToContentsSeeDetail()
            })
            changeMenu.observe(this@BookActivity, Observer {
                bookMenuChangeBottomSheetDialogFragment = BookMenuChangeBottomSheetDialogFragment()
                val bundle = Bundle()
                Log.i(TAG, "현재 상태는? " + bookReadingState)
                bundle.putString("state", bookReadingState)
                bookMenuChangeBottomSheetDialogFragment.setArguments(bundle)
                bookMenuChangeBottomSheetDialogFragment.show(supportFragmentManager, "tag")
            })
            selectedMenu.observe(this@BookActivity, Observer {
                Log.i(TAG, "메뉴 변경 $it")
                when (it) {
                    getString(R.string.before_read) -> {
                        bookReadingState = getString(R.string.before_read)
                    }
                    getString(R.string.reading) -> {
                        bookReadingState = getString(R.string.reading)
                    }
                    getString(R.string.end_read) -> {
                        bookReadingState = getString(R.string.end_read)
                    }
                }

            })
            clickFloaing.observe(this@BookActivity, Observer {
                if(currentTab.equals(BookViewModel.TEXT)) {
                    goToWriteTextMemoActivity()
                } else if(currentTab.equals(BookViewModel.IMAGE)) {
                    showToast("이미지 눌림")
                }
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
                tab.id = position // 각 탭 구별을 위한 아이디 부여
            }
            else if(position == 1) {
                tab.setIcon(R.drawable.ic_image_24dp)
                tab.id = position // 각 탭 구별을 위한 아이디 부여
            }

        }).attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // 탭이 선택 되었을 때
                Log.i(TAG ,"선택된 탭: ${tab?.id} 0: 텍스트, 1: 이미지")
                if(tab?.id == 0) {
                    bookViewModel.currentTab = BookViewModel.TEXT
                } else if(tab?.id == 1) {
                    bookViewModel.currentTab = BookViewModel.IMAGE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 탭이 선택되지 않은 상태로 변경 되었을 때
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 이미 선택된 탭이 다시 선택 되었을 때
            }
        })
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

    fun goToWriteTextMemoActivity() {
        var intent = Intent(this, WriteTextMemoActivity::class.java)
        intent.putExtra("book_idx", bookViewModel.book_idx)
        intent.putExtra("bookTitle", bookViewModel.bookTitle.value)
        startActivity(intent)
//        finish()
    }
}