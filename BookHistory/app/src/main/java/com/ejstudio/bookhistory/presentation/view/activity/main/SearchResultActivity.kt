package com.ejstudio.bookhistory.presentation.view.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivitySearchResultBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.adapter.main.SearchBookAdapter
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.SearchResultViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchResultActivity : BaseActivity<ActivitySearchResultBinding>(R.layout.activity_search_result) {

    private val TAG: String? = SearchResultActivity::class.java.simpleName
    private val searchResultViewModel: SearchResultViewModel by viewModel()
    var searchBookAdapter = SearchBookAdapter()

    var pastVisiblesItems:Int = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_search_result)

        binding.searchResultViewModel = searchResultViewModel

        binding.etSearch.requestFocus()

        recvIntent()
        settingRecyclerView()
        viewModelCallback()

    }

    fun recvIntent() {
        searchResultViewModel.inputSearch.value = intent.getStringExtra("searchKeyword")
        searchResultViewModel.searchButton()
    }

    fun settingRecyclerView() {
        var layoutmanager = LinearLayoutManager(binding.rcBookResult.context)
        binding.rcBookResult.layoutManager = layoutmanager
        binding.rcBookResult.adapter = searchBookAdapter

        binding.rcBookResult.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { // 스크롤이 아래로 향하고 있는지 확인
                    visibleItemCount = layoutmanager.getChildCount()
                    totalItemCount = layoutmanager.getItemCount()
                    pastVisiblesItems = layoutmanager.findFirstVisibleItemPosition()

                    if (searchResultViewModel.isLoading.value == false) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            searchResultViewModel.getSearchBook()
                        }
                    }

                }
            }
        })
    }

    fun viewModelCallback() {
        with(searchResultViewModel) {
            backButton.observe(this@SearchResultActivity, Observer {
                activityBackButton()
            })
            bookList.observe(this@SearchResultActivity, Observer {
                Log.i(TAG, it.get(0).title + "수신중입니다.")
                searchBookAdapter.updataList(it)
            })

        }
    }

    fun activityBackButton() {
        onBackPressed()
    }
}