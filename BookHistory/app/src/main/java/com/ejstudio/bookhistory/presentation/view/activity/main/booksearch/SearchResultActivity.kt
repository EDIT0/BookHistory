package com.ejstudio.bookhistory.presentation.view.activity.main.booksearch

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivitySearchResultBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.adapter.main.booksearch.SearchBookAdapter
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booksearch.SearchResultViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.ejstudio.bookhistory.util.Converter
import android.content.Intent
import com.ejstudio.bookhistory.domain.model.SearchBookModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.login.FindPasswordViewModel


class SearchResultActivity : BaseActivity<ActivitySearchResultBinding>(R.layout.activity_search_result) {

    private val TAG: String? = SearchResultActivity::class.java.simpleName
    private val searchResultViewModel: SearchResultViewModel by viewModel()
    var searchBookAdapter = SearchBookAdapter()

    var pastVisiblesItems:Int = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0

    private lateinit var manager: InputMethodManager

    lateinit var dataEmptyView: View
    lateinit var emptyImage: ImageView
    lateinit var emptyTextTitle: TextView
    lateinit var emptyTextSubTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_search_result)

        binding.searchResultViewModel = searchResultViewModel

        binding.etSearch.requestFocus()

        recvIntent()
        keyboardSetting()
        dataEmptyScreenSetting()
        settingRecyclerView()
        viewModelCallback()
        buttonClickListener()

    }

    fun recvIntent() {
        searchResultViewModel.inputSearch.value = intent.getStringExtra("searchKeyword")
        searchResultViewModel.searchButton() // 받은 데이터가 있으면 자동 검색
    }

    fun keyboardSetting() {
        manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        binding.etSearch.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
                if (p2?.action == KeyEvent.ACTION_DOWN && p1 == KeyEvent.KEYCODE_ENTER) {
                    searchResultViewModel.searchButton()
                    return true
                }
                return false
            }
        })
    }

    fun dataEmptyScreenSetting() {
        dataEmptyView = this.findViewById<View>(R.id.include_data_empty_screen)
        emptyImage = dataEmptyView.findViewById<ImageView>(R.id.iv_emptyImage)
        emptyTextTitle = dataEmptyView.findViewById<TextView>(R.id.tv_emptyTextTitle)
        emptyTextSubTitle = dataEmptyView.findViewById<TextView>(R.id.tv_emptyTextSubTitle)
    }

    fun settingRecyclerView() {
        var layoutmanager = LinearLayoutManager(binding.rcBookResult.context)
        binding.rcBookResult.layoutManager = layoutmanager
        binding.rcBookResult.adapter = searchBookAdapter

        binding.rcBookResult.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        binding.rcBookResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
            keyHidden.observe(this@SearchResultActivity, Observer {
                manager.hideSoftInputFromWindow(getCurrentFocus()?.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
            })
            isData.observe(this@SearchResultActivity, Observer {
                Glide.with(binding.root)
                    .load(R.drawable.ic_sentiment_neutral_24dp)
                    .override(Converter.dpToPx(this@SearchResultActivity, 24),Converter.dpToPx(this@SearchResultActivity, 24))
                    .into(emptyImage)
                emptyTextTitle.text = "'검색어'를 찾을 수 없습니다"
                emptyTextSubTitle.text = "단어의 철자나 띄어쓰기를 확인해주세요"
            })
            requestSnackbar.observe(this@SearchResultActivity, Observer {
                when(snackbarMessage) {
                    SearchResultViewModel.MessageSet.NETWORK_NOT_CONNECTED.toString() -> {
                        snackbarMessage = getString(R.string.NETWORK_NOT_CONNECTED)
                    }
                }
                showSnackbar(snackbarMessage)
            })
        }
    }

    fun buttonClickListener() {
        searchBookAdapter.setOnItemClickListener(object : SearchBookAdapter.OnItemClickListener {
            override fun onItemClick(holder: SearchBookAdapter.ViewHolder?, view: View?, position: Int) {
                goToBookDetailPageActivity(position)
            }
        })
    }

    fun goToBookDetailPageActivity(position: Int) {
        val intent = Intent(this, BookDetailPageActivity::class.java)
        intent.putExtra("authors", searchResultViewModel.bookList.value!!.get(position).authors as ArrayList<SearchBookModel.Document>)
        intent.putExtra("contents", searchResultViewModel.bookList.value!!.get(position).contents.toString())
        intent.putExtra("datetime", searchResultViewModel.bookList.value!!.get(position).datetime.toString())
        intent.putExtra("isbn", searchResultViewModel.bookList.value!!.get(position).isbn.toString())
        intent.putExtra("price", searchResultViewModel.bookList.value!!.get(position).price.toString())
        intent.putExtra("publisher", searchResultViewModel.bookList.value!!.get(position).publisher.toString())
        intent.putExtra("sale_price", searchResultViewModel.bookList.value!!.get(position).sale_price.toString())
        intent.putExtra("status", searchResultViewModel.bookList.value!!.get(position).status.toString())
        intent.putExtra("thumbnail", searchResultViewModel.bookList.value!!.get(position).thumbnail.toString())
        intent.putExtra("title", searchResultViewModel.bookList.value!!.get(position).title.toString())
        intent.putExtra("translators", searchResultViewModel.bookList.value!!.get(position).translators as ArrayList<SearchBookModel.Document>)
        intent.putExtra("url", searchResultViewModel.bookList.value!!.get(position).url.toString())
        startActivity(intent)
        overridePendingTransition(R.anim.rightin_activity, R.anim.leftout_activity)
    }

    fun activityBackButton() {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.leftin_activity, R.anim.rightout_activity)
    }
}