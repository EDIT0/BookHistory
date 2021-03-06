package com.ejstudio.bookhistory.presentation.view.activity.main.booksearch

import android.os.Bundle
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityBookDetailPageBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booksearch.BookDetailPageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager


class BookDetailPageActivity : BaseActivity<ActivityBookDetailPageBinding>(R.layout.activity_book_detail_page) {

    private val TAG: String? = BookDetailPageActivity::class.java.simpleName
    private val bookDetailPageViewModel: BookDetailPageViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.bookDetailPageViewModel = bookDetailPageViewModel


        recvIntent()
        settingRecyclerView()
        viewModelCallback()
    }

    fun recvIntent() {
        val intent = getIntent()
        bookDetailPageViewModel.authors = intent.getSerializableExtra("authors").toString()
        bookDetailPageViewModel.authors = bookDetailPageViewModel.authors.substring(1, bookDetailPageViewModel.authors.length - 1)
        bookDetailPageViewModel.contents = intent.getStringExtra("contents").toString() + "..."
        bookDetailPageViewModel.datetime = intent.getStringExtra("datetime").toString()
        bookDetailPageViewModel.datetime = bookDetailPageViewModel.datetime.substring(0, 10)
        bookDetailPageViewModel.isbn = intent.getStringExtra("isbn").toString()
        bookDetailPageViewModel.price = intent.getStringExtra("price").toString()
        bookDetailPageViewModel.publisher = intent.getStringExtra("publisher").toString()
        bookDetailPageViewModel.sale_price = intent.getStringExtra("sale_price").toString()
        bookDetailPageViewModel.status = intent.getStringExtra("status").toString()
        bookDetailPageViewModel.thumbnail = intent.getStringExtra("thumbnail").toString()
        bookDetailPageViewModel.title = intent.getStringExtra("title").toString()
        bookDetailPageViewModel.translators = intent.getSerializableExtra("translators").toString()
        bookDetailPageViewModel.url = intent.getStringExtra("url").toString()
        Log.i(TAG, "????????? authors: " + bookDetailPageViewModel.authors)
        Log.i(TAG, "????????? contents: " + bookDetailPageViewModel.contents)
        Log.i(TAG, "????????? datetime: " + bookDetailPageViewModel.datetime)
        Log.i(TAG, "????????? isbn: " + bookDetailPageViewModel.isbn)
        Log.i(TAG, "????????? price: " + bookDetailPageViewModel.price)
        Log.i(TAG, "????????? publisher: " + bookDetailPageViewModel.publisher)
        Log.i(TAG, "????????? sale_price: " + bookDetailPageViewModel.sale_price)
        Log.i(TAG, "????????? thumbnail: " + bookDetailPageViewModel.thumbnail)
        Log.i(TAG, "????????? title: " + bookDetailPageViewModel.title)
        Log.i(TAG, "????????? translators: " + bookDetailPageViewModel.translators)
        Log.i(TAG, "????????? url: " + bookDetailPageViewModel.url)
    }

    fun settingRecyclerView() {
        binding.scrollView.setOnScrollChangeListener { view, i, i2, i3, i4 ->
            binding.actionBarBackgroundView.isSelected = binding.scrollView.canScrollVertically(-1)
        }
    }

    fun viewModelCallback() {
        with(bookDetailPageViewModel) {
            backButton.observe(this@BookDetailPageActivity, Observer {
                activityBackButton()
            })
            requestSnackbar.observe(this@BookDetailPageActivity, Observer {
                showSnackbar(snackbarMessage)
            })
            contentsSeeDetail.observe(this@BookDetailPageActivity, Observer {
                goToContentsSeeDetail()
            })
            requestSnackbar.observe(this@BookDetailPageActivity, Observer {
                when(snackbarMessage) {
                    BookDetailPageViewModel.MessageSet.NETWORK_NOT_CONNECTED.toString() -> {
                        snackbarMessage = getString(R.string.NETWORK_NOT_CONNECTED)
                    }
                    BookDetailPageViewModel.MessageSet.COMPLETE_ADD_BOOK.toString() -> {
                        snackbarMessage = getString(R.string.COMPLETE_ADD_BOOK)
                    }
                    BookDetailPageViewModel.MessageSet.ALREADY_GET_BOOK.toString() -> {
                        snackbarMessage = getString(R.string.ALREADY_GET_BOOK)
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
        super.onBackPressed()
        overridePendingTransition(R.anim.leftin_activity, R.anim.rightout_activity)
    }

    fun goToContentsSeeDetail() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(bookDetailPageViewModel.url))
        startActivity(browserIntent)
    }
}