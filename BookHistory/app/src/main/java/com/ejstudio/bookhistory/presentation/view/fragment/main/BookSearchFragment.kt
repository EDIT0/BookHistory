package com.ejstudio.bookhistory.presentation.view.fragment.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.FragmentBookSearchBinding
import com.ejstudio.bookhistory.presentation.view.activity.main.booksearch.SearchActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookSearchFragment : Fragment() {

    private val TAG = BookSearchFragment::class.java.simpleName
    lateinit var binding: FragmentBookSearchBinding
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_search, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        viewModelCallback()
        buttonClickListener()

        return binding.root
    }

    fun viewModelCallback() {
        with(mainViewModel) {
//            goToSearch.observe(viewLifecycleOwner, Observer {
//                Log.i(TAG, "이거 왜 눌림?")
//                goToSearchActivity("")
//            })

        }
    }

    fun buttonClickListener() {
        binding.searchBackgroundView.setOnClickListener {
            goToSearchActivity("")
        }
    }

    fun goToSearchActivity(recentSearchs: String) {
        val intent: Intent = Intent(activity, SearchActivity::class.java)
        if(!recentSearchs.equals("")) {
            intent.putExtra("recentSearchs", "리사이클러뷰 아이템 중 하나")
        }
        startActivity(Intent(activity, SearchActivity::class.java))
    }

}