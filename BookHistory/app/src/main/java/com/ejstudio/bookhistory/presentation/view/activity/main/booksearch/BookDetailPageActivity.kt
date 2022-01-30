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


class BookDetailPageActivity : BaseActivity<ActivityBookDetailPageBinding>(R.layout.activity_book_detail_page) {

    private val TAG: String? = BookDetailPageActivity::class.java.simpleName
    private val bookDetailPageViewModel: BookDetailPageViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.bookDetailPageViewModel = bookDetailPageViewModel


        recvIntent()
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
        Log.i(TAG, "리시브 authors: " + bookDetailPageViewModel.authors)
        Log.i(TAG, "리시브 contents: " + bookDetailPageViewModel.contents)
        Log.i(TAG, "리시브 datetime: " + bookDetailPageViewModel.datetime)
        Log.i(TAG, "리시브 isbn: " + bookDetailPageViewModel.isbn)
        Log.i(TAG, "리시브 price: " + bookDetailPageViewModel.price)
        Log.i(TAG, "리시브 publisher: " + bookDetailPageViewModel.publisher)
        Log.i(TAG, "리시브 sale_price: " + bookDetailPageViewModel.sale_price)
        Log.i(TAG, "리시브 thumbnail: " + bookDetailPageViewModel.thumbnail)
        Log.i(TAG, "리시브 title: " + bookDetailPageViewModel.title)
        Log.i(TAG, "리시브 translators: " + bookDetailPageViewModel.translators)
        Log.i(TAG, "리시브 url: " + bookDetailPageViewModel.url)
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
        }
    }

    fun activityBackButton() {
        onBackPressed()
    }

    fun goToContentsSeeDetail() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(bookDetailPageViewModel.url))
        startActivity(browserIntent)
    }
}