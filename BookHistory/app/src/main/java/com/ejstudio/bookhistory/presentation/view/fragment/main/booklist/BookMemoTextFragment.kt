package com.ejstudio.bookhistory.presentation.view.fragment.main.booklist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ejstudio.bookhistory.R

class BookMemoTextFragment : Fragment() {

    private val TAG = BookMemoTextFragment::class.java.simpleName

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_memo_text, container, false)
    }
}