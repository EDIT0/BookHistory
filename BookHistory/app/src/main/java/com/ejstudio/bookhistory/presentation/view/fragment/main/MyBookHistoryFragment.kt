package com.ejstudio.bookhistory.presentation.view.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.FragmentBookListBinding
import com.ejstudio.bookhistory.databinding.FragmentMyBookHistoryBinding
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyBookHistoryFragment : Fragment() {

    lateinit var binding: FragmentMyBookHistoryBinding
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_book_history, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        return binding.root
    }

}