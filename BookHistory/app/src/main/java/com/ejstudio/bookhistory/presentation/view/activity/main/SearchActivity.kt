package com.ejstudio.bookhistory.presentation.view.activity.main

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityLoginBinding
import com.ejstudio.bookhistory.databinding.ActivitySearchBinding
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.activity.login.LoginActivity
import com.ejstudio.bookhistory.presentation.view.adapter.main.SearchBookAdapter
import com.ejstudio.bookhistory.presentation.view.viewmodel.login.LoginViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search) {

    private val TAG: String? = SearchActivity::class.java.simpleName
    private val searchViewModel: SearchViewModel by viewModel()
    var searchBookAdapter = SearchBookAdapter()

    var pastVisiblesItems:Int = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.searchViewModel = searchViewModel

        binding.etSearch.requestFocus()

        settingRecyclerView()
        viewModelCallback()
    }

    fun settingRecyclerView() {
        var layoutmanager = LinearLayoutManager(binding.rcSearchBook.context)
        binding.rcSearchBook.layoutManager = layoutmanager
        binding.rcSearchBook.adapter = searchBookAdapter

        binding.rcSearchBook.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { // 스크롤이 아래로 향하고 있는지 확인
                    visibleItemCount = layoutmanager.getChildCount()
                    totalItemCount = layoutmanager.getItemCount()
                    pastVisiblesItems = layoutmanager.findFirstVisibleItemPosition()

                    if (searchViewModel.isLoading.value == false) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            searchViewModel.getSearchBook()
                        }
                    }

                }
            }
        })
    }

    fun viewModelCallback() {
        with(searchViewModel) {
            backButton.observe(this@SearchActivity, Observer {
                activityBackButton()
            })
            bookList.observe(this@SearchActivity, Observer {
                Log.i(TAG, it.get(0).title + "수신중입니다.")
                searchBookAdapter.updataList(it)
            })

        }
    }

    fun activityBackButton() {
        onBackPressed()
    }
}