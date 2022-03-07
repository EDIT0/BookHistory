package com.ejstudio.bookhistory.presentation.view.fragment.main.booklist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.FragmentBookMemoTextBinding
import com.ejstudio.bookhistory.presentation.view.activity.main.booklist.BookActivity
import com.ejstudio.bookhistory.presentation.view.activity.main.booklist.SeeTextMemoActivity
import com.ejstudio.bookhistory.presentation.view.activity.main.booklist.WriteTextMemoActivity
import com.ejstudio.bookhistory.presentation.view.adapter.main.booklist.BookMemoTextAdapter
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.BookViewModel
import com.ejstudio.bookhistory.util.Converter
import androidx.recyclerview.widget.RecyclerView




class BookMemoTextFragment : Fragment() {

    private val TAG = BookMemoTextFragment::class.java.simpleName

    lateinit var binding: FragmentBookMemoTextBinding
    //    private val mainViewModel: MainViewModel by viewModel()
    lateinit var bookViewModel: BookViewModel
    var bookTextMemoAdapter = BookMemoTextAdapter()

    lateinit var dataEmptyView: View
    lateinit var emptyImage: ImageView
    lateinit var emptyTextTitle: TextView
    lateinit var emptyTextSubTitle: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_memo_text, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        bookViewModel = (activity as BookActivity).bookViewModel
        binding.bookViewModel = bookViewModel

        settingRecyclerView()
        dataEmptyScreenSetting()
        viewModelCallback()
        buttonClickListener()

        return binding.root
    }
    
    fun settingRecyclerView(){
        var layoutmanager = LinearLayoutManager(binding.rcTextMemoList.context)
        binding.rcTextMemoList.layoutManager = layoutmanager
        binding.rcTextMemoList.adapter = bookTextMemoAdapter
//        binding.rcTextMemoList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        // 리사이클러뷰가 상단을 찍을 시 상위 네스티드 스크롤뷰를 켠다.
        binding.rcTextMemoList.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!binding.rcTextMemoList.canScrollVertically(-1)) {
//                    Log.i(TAG, "Top of list")
                    BookActivity.isNestedScrolling.setValue(true)
                } else if (!binding.rcTextMemoList.canScrollVertically(1)) {
//                    Log.i(TAG, "End of list")
                } else {
//                    Log.i(TAG, "idle")
                }
            }
        })
    }

    fun dataEmptyScreenSetting() {
        dataEmptyView = binding.root.findViewById<View>(R.id.include_data_empty_screen)
        emptyImage = dataEmptyView.findViewById<ImageView>(R.id.iv_emptyImage)
        emptyTextTitle = dataEmptyView.findViewById<TextView>(R.id.tv_emptyTextTitle)
        emptyTextSubTitle = dataEmptyView.findViewById<TextView>(R.id.tv_emptyTextSubTitle)
    }

    fun viewModelCallback(){
        with(bookViewModel) {
            textMemoList.observe(viewLifecycleOwner, Observer {
                Log.i(TAG, "텍스트 메모 갯수: " + it.size)
                Log.i(TAG, "텍스트 가져온 idx: ${book_idx}")
                if(it == null || it.size == 0) {
                    showDataEmptyScreen()
                    bookTextMemoAdapter.updataList(it)
                } else {
                    hideDataEmptyScreen()
                    bookTextMemoAdapter.updataList(it)
                }

            })
            isData.observe(viewLifecycleOwner, Observer {
                Glide.with(binding.root)
                    .load(R.drawable.ic_sentiment_satisfied_24dp)
                    .override(
                        Converter.dpToPx(binding.root.context, 24),
                        Converter.dpToPx(binding.root.context, 24))
                    .into(emptyImage)
                emptyTextTitle.text = "기록하고\n나중에 꺼내보는\n즐거움이 있어요"
            })
        }
    }

    fun buttonClickListener() {
        bookTextMemoAdapter.setOnBookMemoTextClickListener(object : BookMemoTextAdapter.OnBookMemoTextClickListener{
            override fun onItemClick(holder: BookMemoTextAdapter.ViewHolder?, view: View?, position: Int) {
                goToSeeTextMemoActivity(position)
            }

        })
    }

    fun goToSeeTextMemoActivity(position: Int) {
        var intent = Intent(binding.root.context, SeeTextMemoActivity::class.java)
        intent.putExtra("textMemoIdx", bookViewModel.textMemoList.value?.get(position)?.idx)
        intent.putExtra("bookTitle", bookViewModel.bookTitle.value.toString().trim())
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.fade_in,R.anim.not_move_activity)
    }
}