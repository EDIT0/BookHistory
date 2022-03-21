package com.ejstudio.bookhistory.presentation.view.fragment.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.FragmentBookSearchBinding
import com.ejstudio.bookhistory.domain.model.RecommendBookModel
import com.ejstudio.bookhistory.presentation.view.activity.main.MainActivity
import com.ejstudio.bookhistory.presentation.view.activity.main.booksearch.SearchActivity
import com.ejstudio.bookhistory.presentation.view.activity.main.booksearch.SearchResultActivity
import com.ejstudio.bookhistory.presentation.view.adapter.main.booksearch.AlwaysPopularBookAdapter
import com.ejstudio.bookhistory.presentation.view.adapter.main.booksearch.RecentPopularBookAdapter
import com.ejstudio.bookhistory.presentation.view.adapter.main.booksearch.RecommendBookAdapter
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.ejstudio.bookhistory.util.OffsetItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class BookSearchFragment : Fragment() {

    private val TAG = BookSearchFragment::class.java.simpleName

    private var mAdView: AdView? = null

    lateinit var binding: FragmentBookSearchBinding
//    private val mainViewModel: MainViewModel by viewModel()
    lateinit var mainViewModel: MainViewModel

    var recentPopularBookAdapter = RecentPopularBookAdapter()
    var recommendBookAdapter = RecommendBookAdapter()
    var alwaysPopularBookAdapter = AlwaysPopularBookAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_search, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        mainViewModel = (activity as MainActivity).mainViewModel
        binding.mainViewModel = mainViewModel

        settingAdView()
        settingRecyclerView()
        viewModelCallback()
        buttonClickListener()

        return binding.root
    }

    fun settingAdView() {
        MobileAds.initialize(binding.root.context)
        mAdView = binding.adView
        val adRequest: AdRequest = AdRequest.Builder().build()

        mAdView!!.loadAd(adRequest)
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume 애드몹 갱신2 ${mainViewModel.adCount}")
        mainViewModel.adCount++
        if(mainViewModel.adCount >= 5) {
            Log.i(TAG, "애드몹 갱신2")
            val adRequest: AdRequest = AdRequest.Builder().build()
            mAdView!!.loadAd(adRequest)
            mainViewModel.adCount = 0
        }
    }

    fun settingRecyclerView() {
        // 요즘 많이 읽는 책들
        var layoutmanager1 = LinearLayoutManager(binding.rcRecentPopularBook.context)
        layoutmanager1.orientation = LinearLayoutManager.HORIZONTAL
        binding.rcRecentPopularBook.layoutManager = layoutmanager1
        binding.rcRecentPopularBook.adapter = recentPopularBookAdapter

        val itemDecoration1 = OffsetItemDecoration(binding.root.context, 20, 10, 20)
        binding.rcRecentPopularBook.addItemDecoration(itemDecoration1)

        // 당신을 위한 추천
        var layoutmanager2 = LinearLayoutManager(binding.rcRecommendBook.context)
        layoutmanager2.orientation = LinearLayoutManager.HORIZONTAL
        binding.rcRecommendBook.layoutManager = layoutmanager2
        binding.rcRecommendBook.adapter = recommendBookAdapter

        val itemDecoration2 = OffsetItemDecoration(binding.root.context, 20, 10, 20)
        binding.rcRecommendBook.addItemDecoration(itemDecoration2)

        // 오랫동안 인기있는 책
        var layoutmanager3 = LinearLayoutManager(binding.rcAlwaysPopularBook.context)
        layoutmanager3.orientation = LinearLayoutManager.HORIZONTAL
        binding.rcAlwaysPopularBook.layoutManager = layoutmanager3
        binding.rcAlwaysPopularBook.adapter = alwaysPopularBookAdapter

        val itemDecoration3 = OffsetItemDecoration(binding.root.context, 20, 10, 20)
        binding.rcAlwaysPopularBook.addItemDecoration(itemDecoration3)


        binding.scrollView.setOnScrollChangeListener { view, i, i2, i3, i4 ->
            binding.searchBackgroundView.isSelected = binding.scrollView.canScrollVertically(-1)
        }
    }

    fun viewModelCallback() {
        with(mainViewModel) {
//            goToSearch.observe(viewLifecycleOwner, Observer {
//                Log.i(TAG, "이거 왜 눌림?")
//                goToSearchActivity("")
//            })

            recentPopularBookList.observe(viewLifecycleOwner, Observer {
                recentPopularBookAdapter.updataList(it)
            })
            recommendBookList.observe(viewLifecycleOwner, Observer {
                // 최대 200개 중 10개만
                var arr = ArrayList<RecommendBookModel.Response.Doc>()
                if(it.size > 10) {
                    for (i in 0 until 10) {
                        arr.add(it.get(i))
                    }
                    recommendBookAdapter.updataList(arr)
                } else {
                    recommendBookAdapter.updataList(it)
                }
            })
            alwaysPopularBookList.observe(viewLifecycleOwner, Observer {
                alwaysPopularBookAdapter.updataList(it)
            })
        }
    }

    fun buttonClickListener() {
        binding.searchBackgroundView.setOnClickListener {
            goToSearchActivity("")
        }
        recentPopularBookAdapter.setOnRecentPopularBookClickListener(object : RecentPopularBookAdapter.OnRecentPopularBookClickListener{
            override fun onItemClick(holder: RecentPopularBookAdapter.ViewHolder?, view: View?, position: Int) {
                goToSearchResultActivity(mainViewModel.recentPopularBookList.value?.get(position)?.doc?.isbn13.toString().trim())
            }
        })
        recommendBookAdapter.setOnRecommendBookClickListener(object : RecommendBookAdapter.OnRecommendBookClickListener{
            override fun onItemClick(holder: RecommendBookAdapter.ViewHolder?, view: View?, position: Int) {
                goToSearchResultActivity(mainViewModel.recommendBookList.value?.get(position)?.book?.isbn13.toString().trim())
            }
        })
        alwaysPopularBookAdapter.setOnAlwaysPopularBookClickListener(object : AlwaysPopularBookAdapter.OnAlwaysPopularBookClickListener{
            override fun onItemClick(holder: AlwaysPopularBookAdapter.ViewHolder?, view: View?, position: Int) {
                goToSearchResultActivity(mainViewModel.alwaysPopularBookList.value?.get(position)?.doc?.isbn13.toString().trim())
            }
        })
    }

    fun goToSearchActivity(recentSearchs: String) {
        val intent: Intent = Intent(activity, SearchActivity::class.java)
        if(!recentSearchs.equals("")) {
            intent.putExtra("recentSearchs", "리사이클러뷰 아이템 중 하나")
        }
        startActivity(Intent(activity, SearchActivity::class.java))
        requireActivity().overridePendingTransition(R.anim.rightin_activity, R.anim.leftout_activity)
    }

    fun goToSearchResultActivity(keyword: String) {
        if (!mainViewModel.checkNetworkState()) return
        val intent: Intent = Intent(activity, SearchResultActivity::class.java)
        intent.putExtra("searchKeyword", keyword)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.rightin_activity, R.anim.leftout_activity)
    }

}