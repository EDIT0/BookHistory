package com.ejstudio.bookhistory.presentation.view.fragment.main.booklist

import android.content.Intent
import android.graphics.Point
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.FragmentBookMemoImageBinding
import com.ejstudio.bookhistory.databinding.FragmentBookMemoTextBinding
import com.ejstudio.bookhistory.presentation.view.activity.main.booklist.BookActivity
import com.ejstudio.bookhistory.presentation.view.adapter.main.booklist.BookMemoImageAdapter
import com.ejstudio.bookhistory.presentation.view.adapter.main.booklist.BookMemoTextAdapter
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.BookViewModel
import com.ejstudio.bookhistory.util.Converter
import android.view.Display
import androidx.recyclerview.widget.RecyclerView
import com.ejstudio.bookhistory.presentation.view.activity.main.booklist.SeeImageMemoActivity
import com.ejstudio.bookhistory.presentation.view.activity.main.booklist.SeeTextMemoActivity


class BookMemoImageFragment : Fragment() {

    private val TAG = BookMemoImageFragment::class.java.simpleName

    lateinit var binding: FragmentBookMemoImageBinding
    //    private val mainViewModel: MainViewModel by viewModel()
    lateinit var bookViewModel: BookViewModel
    var bookImageMemoAdapter = BookMemoImageAdapter()

    lateinit var dataEmptyView: View
    lateinit var emptyImage: ImageView
    lateinit var emptyTextTitle: TextView
    lateinit var emptyTextSubTitle: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_memo_image, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        bookViewModel = (activity as BookActivity).bookViewModel
        binding.bookViewModel = bookViewModel


        val display: Display = requireActivity().getWindowManager().getDefaultDisplay() // in Activity
        val size = Point()
        display.getRealSize(size) // or getSize(size)

        val width: Int = size.x
        bookImageMemoAdapter.setWidth(width)

        settingRecyclerView()
        dataEmptyScreenSetting()
        viewModelCallback()
        buttonClickListener()

        return binding.root
    }

    fun settingRecyclerView(){
        var gridLayoutManager = GridLayoutManager(binding.rcImageMemoList.context, 2)
        binding.rcImageMemoList.layoutManager = gridLayoutManager
        binding.rcImageMemoList.adapter = bookImageMemoAdapter
//        binding.rcTextMemoList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        // 리사이클러뷰가 상단을 찍을 시 상위 네스티드 스크롤뷰를 켠다.
        binding.rcImageMemoList.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!binding.rcImageMemoList.canScrollVertically(-1)) {
//                    Log.i(TAG, "Top of list")
                    BookActivity.isNestedScrolling.setValue(true)
                } else if (!binding.rcImageMemoList.canScrollVertically(1)) {
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
            imageMemoList.observe(viewLifecycleOwner, Observer {
                Log.i(TAG, "이미지 메모 갯수: " + it.size)
                Log.i(TAG, "이미지 가져온 idx: ${book_idx}")
                if(it == null || it.size == 0) {
                    showDataEmptyScreen()
                    bookImageMemoAdapter.updataList(it)
                } else {
                    hideDataEmptyScreen()
                    bookImageMemoAdapter.updataList(it)
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
        bookImageMemoAdapter.setOnBookMemoImageClickListener(object : BookMemoImageAdapter.OnBookMemoImageClickListener{
            override fun onItemClick(holder: BookMemoImageAdapter.ViewHolder?, view: View?, position: Int) {
                goToSeeImageMemoActivity(position)
            }

        })
    }

    fun goToSeeImageMemoActivity(position: Int) {
        var intent = Intent(binding.root.context, SeeImageMemoActivity::class.java)
        intent.putExtra("imageMemoIdx", bookViewModel.imageMemoList.value?.get(position)?.idx)
        intent.putExtra("imageUrl", bookViewModel.imageMemoList.value?.get(position)?.memo_image)
        intent.putExtra("bookTitle", bookViewModel.bookTitle.value.toString().trim())
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.not_move_activity)
    }



}