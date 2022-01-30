package com.ejstudio.bookhistory.presentation.view.activity.main.booksearch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivitySearchBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.adapter.main.booksearch.RecentSearchesAdapter
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booksearch.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search) {

    private val TAG: String? = SearchActivity::class.java.simpleName
    private val searchViewModel: SearchViewModel by viewModel()
    var recentSearchesAdapter = RecentSearchesAdapter()

    private lateinit var manager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.searchViewModel = searchViewModel

        binding.etSearch.requestFocus()

        keyboardSetting()
        settingRecyclerView()
        viewModelCallback()
        buttonClickListener()
    }

    fun keyboardSetting() {
        manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        binding.etSearch.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            when (keyCode) {
                KeyEvent.KEYCODE_ENTER -> searchViewModel.searchButton()
            }
            true
        })
    }

    fun settingRecyclerView() {
        var layoutmanager = LinearLayoutManager(binding.rcRecentSearchs.context)
        binding.rcRecentSearchs.layoutManager = layoutmanager
        binding.rcRecentSearchs.adapter = recentSearchesAdapter

//        binding.rcRecentSearchs.addOnScrollListener(object :
//            RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                if (dy > 0) { // 스크롤이 아래로 향하고 있는지 확인
//                    visibleItemCount = layoutmanager.getChildCount()
//                    totalItemCount = layoutmanager.getItemCount()
//                    pastVisiblesItems = layoutmanager.findFirstVisibleItemPosition()
//
//                    if (searchViewModel.isLoading.value == false) {
//                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
////                            searchViewModel.getSearchBook()
//                        }
//                    }
//
//                }
//            }
//        })
    }

    fun viewModelCallback() {
        with(searchViewModel) {
            backButton.observe(this@SearchActivity, Observer {
                activityBackButton()
            })
            goToSearchResult.observe(this@SearchActivity, Observer {
                goToSearchResultActivity(searchViewModel.inputSearch.value.toString().trim())
            })
            recentSearchesList.observe(this@SearchActivity, Observer {
                recentSearchesAdapter.updataList(it)
            })

        }
    }

    fun buttonClickListener() {
        recentSearchesAdapter.setOnDeleteRecentSearchesClickListener(object : RecentSearchesAdapter.OnDeleteRecentSearchesClickListener {
            override fun onItemClick(holder: RecentSearchesAdapter.ViewHolder?, view: View?, position: Int) {
                // 클릭된 최근검색어 idx로 삭제 요청
                searchViewModel.deleteRecentSearches(searchViewModel.recentSearchesList.value!!.get(position).idx)
            }

        })

        recentSearchesAdapter.setOnRecentSearchesClickListener(object : RecentSearchesAdapter.OnRecentSearchesClickListener {
            override fun onItemClick(holder: RecentSearchesAdapter.ViewHolder?, view: View?, position: Int) {
                goToSearchResultActivity(searchViewModel.recentSearchesList.value!!.get(position).searchs)
            }

        })
    }

//    fun settingRecyclerView() {
//        var layoutmanager = LinearLayoutManager(binding.rcRecentSearchs.context)
//        binding.rcRecentSearchs.layoutManager = layoutmanager
//        binding.rcRecentSearchs.adapter = searchBookAdapter
//
//        binding.rcRecentSearchs.addOnScrollListener(object :
//            RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                if (dy > 0) { // 스크롤이 아래로 향하고 있는지 확인
//                    visibleItemCount = layoutmanager.getChildCount()
//                    totalItemCount = layoutmanager.getItemCount()
//                    pastVisiblesItems = layoutmanager.findFirstVisibleItemPosition()
//
//                    if (searchViewModel.isLoading.value == false) {
//                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
//                            searchViewModel.getSearchBook()
//                        }
//                    }
//
//                }
//            }
//        })
//    }
//
//    fun viewModelCallback() {
//        with(searchViewModel) {
//            backButton.observe(this@SearchActivity, Observer {
//                activityBackButton()
//            })
//            bookList.observe(this@SearchActivity, Observer {
//                Log.i(TAG, it.get(0).title + "수신중입니다.")
//                searchBookAdapter.updataList(it)
//            })
//
//        }
//    }

    fun activityBackButton() {
        onBackPressed()
    }

    fun goToSearchResultActivity(keyword: String) {
        val intent: Intent = Intent(this, SearchResultActivity::class.java)
//        searchViewModel.inputSearch.value.toString().trim()
        intent.putExtra("searchKeyword", keyword)
        searchViewModel.inputSearch.value = ""
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "마지막 페이지 onResume()")
        binding.etSearch.requestFocus()
        manager.showSoftInput(binding.etSearch, 0);
    }

    override fun onDestroy() {
        manager.hideSoftInputFromWindow(getCurrentFocus()?.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
        super.onDestroy()
    }
}