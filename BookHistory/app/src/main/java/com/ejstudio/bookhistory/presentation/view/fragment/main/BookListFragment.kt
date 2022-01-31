package com.ejstudio.bookhistory.presentation.view.fragment.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.FragmentBookListBinding
import com.ejstudio.bookhistory.presentation.view.activity.main.MainActivity
import com.ejstudio.bookhistory.presentation.view.adapter.main.booklist.BookListAdapter
import com.ejstudio.bookhistory.presentation.view.fragment.login.ToSBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookListMenuSelectionBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import com.ejstudio.bookhistory.util.Converter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception


class BookListFragment : Fragment() {

    private val TAG: String? = BookListFragment::class.java.simpleName

    lateinit var binding: FragmentBookListBinding
//    private val mainViewModel: MainViewModel by viewModel()
    lateinit var mainViewModel: MainViewModel
    var bookListAdapter = BookListAdapter()
    private lateinit var bottomSheet: BookListMenuSelectionBottomSheetDialogFragment

    lateinit var dataEmptyView: View
    lateinit var emptyImage: ImageView
    lateinit var emptyTextTitle: TextView
    lateinit var emptyTextSubTitle: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        mainViewModel = (activity as MainActivity).mainViewModel
        binding.mainViewModel = mainViewModel

        Log.i(TAG, "코드1: ${mainViewModel.hashCode()}")

        settingRecyclerView()
        dataEmptyScreenSetting()
        viewModelCallback()
        buttonClickListener()

        return binding.root
    }

    fun settingRecyclerView() {
        var layoutmanager = LinearLayoutManager(binding.rcBookList.context)
        binding.rcBookList.layoutManager = layoutmanager
        binding.rcBookList.adapter = bookListAdapter
        binding.rcBookList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
    }

    fun dataEmptyScreenSetting() {
        dataEmptyView = binding.root.findViewById<View>(R.id.include_data_empty_screen)
        emptyImage = dataEmptyView.findViewById<ImageView>(R.id.iv_emptyImage)
        emptyTextTitle = dataEmptyView.findViewById<TextView>(R.id.tv_emptyTextTitle)
        emptyTextSubTitle = dataEmptyView.findViewById<TextView>(R.id.tv_emptyTextSubTitle)
    }

    fun viewModelCallback() {
        with(mainViewModel) {
            beforeReadBookList.observe(viewLifecycleOwner, Observer {
                try{
                    bookListAdapter.updataList(it)
                    if(it.size == 0) {
                        showDataEmptyScreen()
                    } else {
                        hideDataEmptyScreen()
                    }
                    Log.i(TAG, "현재 읽기 전 책 갯수: ${it.size}")
                } catch (e: Exception){
                    Log.i(TAG, "읽기 전 오류: " + e.message.toString())
                }
            })
            readingBookList.observe(viewLifecycleOwner, Observer {
                try{
                    bookListAdapter.updataList(it)
                    if(it.size == 0) { showDataEmptyScreen() } else { hideDataEmptyScreen() }
                    Log.i(TAG, "현재 읽는 중 책 갯수: ${it.size}")
                } catch (e: Exception){
                    Log.i(TAG, "읽는 중 오류: " + e.message.toString())
                }
            })
            endReadBookList.observe(viewLifecycleOwner, Observer {
                try{
                    bookListAdapter.updataList(it)
                    if(it.size == 0) { showDataEmptyScreen() } else { hideDataEmptyScreen() }
                    Log.i(TAG, "현재 읽은 후 책 갯수: ${it.size}")
                } catch (e: Exception){
                    Log.i(TAG, "읽은 후 오류: " + e.message.toString())
                }
            })
            selectedMenu.observe(viewLifecycleOwner, Observer {
                Log.i(TAG, "메뉴 변경 $it")
                binding.tvSelection.text = it
                bookListAdapter.updataList(listOf())
                when (it) {
                    getString(R.string.before_read) -> {
                        if(beforeReadBookList.value == null || beforeReadBookList.value?.size == 0) {
                            mainViewModel.showDataEmptyScreen()
                        } else {
                            mainViewModel.hideDataEmptyScreen()
                            bookListAdapter.updataList(beforeReadBookList.value!!)
                        }
                    }
                    getString(R.string.reading) -> {
                        if(readingBookList.value == null || readingBookList.value?.size == 0) {
                            mainViewModel.showDataEmptyScreen()
                        } else {
                            mainViewModel.hideDataEmptyScreen()
                            bookListAdapter.updataList(readingBookList.value!!)
                        }
                    }
                    getString(R.string.end_read) -> {
                        if(endReadBookList.value == null || endReadBookList.value?.size == 0) {
                            mainViewModel.showDataEmptyScreen()
                        } else {
                            mainViewModel.hideDataEmptyScreen()
                            bookListAdapter.updataList(endReadBookList.value!!)
                        }
                    }
                }
            })
            isData.observe(viewLifecycleOwner, Observer {
                // 화면 GONE이 제대로 되지 않아서 넣은 코드
                if(it) {dataEmptyView.visibility = View.GONE} else { dataEmptyView.visibility = View.VISIBLE }
                Glide.with(binding.root)
                    .load(R.drawable.ic_sentiment_very_satisfied_24dp)
                    .override(Converter.dpToPx(binding.root.context, 24),Converter.dpToPx(binding.root.context, 24))
                    .into(emptyImage)

                when (_selectedMenu.value.toString()) {
                    getString(R.string.before_read) -> {
                        emptyTextTitle.text = getString(R.string.before_read_book_empty)
                    }
                    getString(R.string.reading) -> {
                        emptyTextTitle.text = getString(R.string.reading_book_empty)
                    }
                    getString(R.string.end_read) -> {
                        emptyTextTitle.text = getString(R.string.end_read_book_empty)
                    }
                }
//                emptyTextTitle.text = "'[검색어]'를 찾을 수 없습니다."
//                emptyTextSubTitle.text = "단어를 철자나 띄어쓰기를 확인해주세요."
            })
        }
    }

    fun buttonClickListener() {
        binding.tvSelection.setOnClickListener {
            bottomSheet = BookListMenuSelectionBottomSheetDialogFragment()
            val bundle = Bundle()
            bundle.putString("state", mainViewModel._selectedMenu.value.toString())
            bottomSheet.setArguments(bundle)
            bottomSheet.show(activity?.supportFragmentManager!!, "tag")
        }
        binding.ibSelectionImage.setOnClickListener {
            bottomSheet = BookListMenuSelectionBottomSheetDialogFragment()
            val bundle = Bundle()
            bundle.putString("state", mainViewModel._selectedMenu.value.toString())
            bottomSheet.setArguments(bundle)
            bottomSheet.show(activity?.supportFragmentManager!!, "tag")
        }
        bookListAdapter.setOnBookClickListener(object : BookListAdapter.OnBookClickListener{
            override fun onItemClick(holder: BookListAdapter.ViewHolder?, view: View?, position: Int) {

            }
        })
    }

}