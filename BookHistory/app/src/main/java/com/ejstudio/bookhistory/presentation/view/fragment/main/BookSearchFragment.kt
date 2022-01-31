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
import com.ejstudio.bookhistory.presentation.view.activity.main.MainActivity
import com.ejstudio.bookhistory.presentation.view.activity.main.booksearch.SearchActivity
import com.ejstudio.bookhistory.presentation.view.activity.main.booksearch.SearchResultActivity
import com.ejstudio.bookhistory.presentation.view.adapter.main.booksearch.RecentPopularBookAdapter
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.ejstudio.bookhistory.util.OffsetItemDecoration




class BookSearchFragment : Fragment() {

    private val TAG = BookSearchFragment::class.java.simpleName
    lateinit var binding: FragmentBookSearchBinding
//    private val mainViewModel: MainViewModel by viewModel()
    lateinit var mainViewModel: MainViewModel
    var recentPopularBookAdapter = RecentPopularBookAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_search, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        mainViewModel = (activity as MainActivity).mainViewModel
        binding.mainViewModel = mainViewModel

        settingRecyclerView()
        viewModelCallback()
        buttonClickListener()

        return binding.root
    }

    fun settingRecyclerView() {
        var layoutmanager = LinearLayoutManager(binding.rcRecentPopularBook.context)
        layoutmanager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rcRecentPopularBook.layoutManager = layoutmanager
        binding.rcRecentPopularBook.adapter = recentPopularBookAdapter

        val itemDecoration = OffsetItemDecoration(binding.root.context, 20, 10, 20)
        binding.rcRecentPopularBook.addItemDecoration(itemDecoration)
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

        }
    }

    fun buttonClickListener() {
        binding.searchBackgroundView.setOnClickListener {
            goToSearchActivity("")
        }
        recentPopularBookAdapter.setOnRecentPopularBookClickListener(object : RecentPopularBookAdapter.OnRecentPopularBookClickListener{
            override fun onItemClick(holder: RecentPopularBookAdapter.ViewHolder?, view: View?, position: Int) {
                val intent: Intent = Intent(activity, SearchResultActivity::class.java)
                intent.putExtra("searchKeyword", mainViewModel.recentPopularBookList.value?.get(position)?.doc?.bookname)
                startActivity(intent)
            }
        })
    }

    fun goToSearchActivity(recentSearchs: String) {
        val intent: Intent = Intent(activity, SearchActivity::class.java)
        if(!recentSearchs.equals("")) {
            intent.putExtra("recentSearchs", "리사이클러뷰 아이템 중 하나")
        }
        startActivity(Intent(activity, SearchActivity::class.java))
    }

}