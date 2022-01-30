package com.ejstudio.bookhistory.presentation.view.fragment.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.FragmentBookListBinding
import com.ejstudio.bookhistory.presentation.view.adapter.main.booklist.BookListAdapter
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception


class BookListFragment : Fragment() {

    private val TAG: String? = BookListFragment::class.java.simpleName

    lateinit var binding: FragmentBookListBinding
    private val mainViewModel: MainViewModel by viewModel()
    var bookListAdapter = BookListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        settingRecyclerView()
        viewModelCallback()

        return binding.root
    }

    fun settingRecyclerView() {
        var layoutmanager = LinearLayoutManager(binding.rcBookList.context)
        binding.rcBookList.layoutManager = layoutmanager
        binding.rcBookList.adapter = bookListAdapter
        binding.rcBookList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
    }

    fun viewModelCallback() {
        with(mainViewModel) {
            beforeReadBookList.observe(viewLifecycleOwner, Observer {
                try{
                    Log.i(TAG, "현재 읽기 전 책 갯수: ${it.size}")
                    bookListAdapter.updataList(it)
                } catch (e: Exception){
                    Log.i(TAG, "읽기 전 오류: " + e.message.toString())
                }
            })
            readingBookList.observe(viewLifecycleOwner, Observer {
                try{
                    Log.i(TAG, "현재 읽는 중 책 갯수: ${it.size}")
                } catch (e: Exception){
                    Log.i(TAG, "읽는 중 오류: " + e.message.toString())
                }
            })
            endReadBookList.observe(viewLifecycleOwner, Observer {
                try{
                    Log.i(TAG, "현재 읽은 후 책 갯수: ${it.size}")
                } catch (e: Exception){
                    Log.i(TAG, "읽은 후 오류: " + e.message.toString())
                }
            })

        }
    }

}