package com.ejstudio.bookhistory.presentation.view.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.FragmentBookListBinding
import com.ejstudio.bookhistory.databinding.FragmentSettingBinding
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : Fragment() {

    lateinit var binding: FragmentSettingBinding
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        return binding.root
    }
}