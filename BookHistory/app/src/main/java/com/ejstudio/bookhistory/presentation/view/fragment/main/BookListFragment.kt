package com.ejstudio.bookhistory.presentation.view.fragment.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.FragmentBookListBinding
import com.ejstudio.bookhistory.presentation.view.activity.main.MainActivity
import com.ejstudio.bookhistory.presentation.view.activity.main.booklist.BookActivity
import com.ejstudio.bookhistory.presentation.view.adapter.main.booklist.BookListAdapter
import com.ejstudio.bookhistory.presentation.view.fragment.login.ToSBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookListMenuSelectionBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import com.ejstudio.bookhistory.util.Converter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception
import java.lang.StringBuilder
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.AdRequest

import com.google.android.gms.ads.MobileAds
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.res.Resources
import android.os.Build
import android.widget.*
import android.widget.DatePicker.OnDateChangedListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.util.UserInfo
import java.lang.reflect.Field
import java.time.LocalDate
import java.util.*


class BookListFragment : Fragment() {

    private val TAG: String? = BookListFragment::class.java.simpleName

    private var mAdView: AdView? = null

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

        settingAdView()
        settingRecyclerView()
        dataEmptyScreenSetting()
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
        Log.i(TAG, "onResume 애드몹 갱신1 ${mainViewModel.adCount}")
        mainViewModel.adCount++
        if(mainViewModel.adCount >= 5) {
            Log.i(TAG, "애드몹 갱신1")
            val adRequest: AdRequest = AdRequest.Builder().build()
            mAdView!!.loadAd(adRequest)
            mainViewModel.adCount = 0
        }
    }

    fun settingRecyclerView() {
        var layoutmanager = LinearLayoutManager(binding.rcBookList.context)
        binding.rcBookList.layoutManager = layoutmanager
        binding.rcBookList.adapter = bookListAdapter
        binding.rcBookList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        binding.nestedScrollview.setOnScrollChangeListener { view, i, i2, i3, i4 ->
            binding.actionBarBackgroundView.isSelected = binding.nestedScrollview.canScrollVertically(-1)
        }
    }

    fun dataEmptyScreenSetting() {
        dataEmptyView = binding.root.findViewById<View>(R.id.include_data_empty_screen)
        emptyImage = dataEmptyView.findViewById<ImageView>(R.id.iv_emptyImage)
        emptyTextTitle = dataEmptyView.findViewById<TextView>(R.id.tv_emptyTextTitle)
        emptyTextSubTitle = dataEmptyView.findViewById<TextView>(R.id.tv_emptyTextSubTitle)
    }

    fun viewModelCallback() {
        with(mainViewModel) {
            totalBookList.observe(viewLifecycleOwner, Observer {
                if(it == null || it.size == 0) {
                    // TODO 책 갯수가 0개면 뭐를 추천해야하는지?
                    getRecommendBookList("9791165341909;")
                } else {
                    var str: StringBuilder = StringBuilder()
                    if(it.size > 10) {
                        for(i in 0 until 10) {
                            if(it.get(i).isbn?.trim()?.length == 13) {
                                continue
                            }
                            if(i == 9) {
                                str.append(it.get(i).isbn?.substring(0,10)?.trim())
                            } else {
                                str.append(it.get(i).isbn?.substring(0,10)?.trim() + ";")
                            }
                        }
                        Log.i(TAG, " ${recommendBookList.value?.size} isbn = " + str.toString())
                        if(recommendBookList.value == null) {
                            getRecommendBookList(str.toString()) // 당신을 위한 추천 책들 호출
                        }
                    } else {
                        for(i in 0 until it.size) {
                            if(it.get(i).isbn?.trim()?.length == 13) {
                                continue
                            }
                            if(i == 0) {
                                str.append(it.get(i).isbn?.substring(0,10)?.trim() + ";")
                            }
                            else if(i == (it.size-1)) {
                                str.append(it.get(i).isbn?.substring(0,10)?.trim())
                            } else {
                                str.append(it.get(i).isbn?.substring(0,10)?.trim() + ";")
                            }
                        }
                        Log.i(TAG, " ${recommendBookList.value?.size} isbn = " + str.toString())
                        if(recommendBookList.value == null) {
                            getRecommendBookList(str.toString()) // 당신을 위한 추천 책들 호출
                        }
                    }
                }
            })
//            busObservable.observe(viewLifecycleOwner, Observer {
//                Log.i(TAG, "코드님: ${it.toString()}")
//            })
            beforeReadBookList.observe(viewLifecycleOwner, Observer {
                Log.i(TAG, "코드응답왔다. ${_selectedMenu.value} ${getString(R.string.before_read)}")
                try{
                    if(_selectedMenu.value.equals(getString(R.string.before_read))) {
                        Log.i(TAG, "읽을 책 순: ${it}")
                        bookListAdapter.updataList(it)
                        if(it.size == 0) { showDataEmptyScreen() } else { hideDataEmptyScreen() }
                        Log.i(TAG, "현재 읽기 전 책 갯수: ${it.size}")
                    }
                } catch (e: Exception){
                    Log.i(TAG, "읽기 전 오류: " + e.message.toString())
                }
            })
            readingBookList.observe(viewLifecycleOwner, Observer {
                try{
                    if(_selectedMenu.value.equals(getString(R.string.reading))) {
                        Log.i(TAG, "읽는 중 순: ${it}")
                        bookListAdapter.updataList(it)
                        if (it.size == 0) { showDataEmptyScreen() } else { hideDataEmptyScreen() }
                        Log.i(TAG, "현재 읽는 중 책 갯수: ${it.size}")
                    }
                } catch (e: Exception){
                    Log.i(TAG, "읽는 중 오류: " + e.message.toString())
                }
            })
            endReadBookList.observe(viewLifecycleOwner, Observer {
                try{
                    if(_selectedMenu.value.equals(getString(R.string.end_read))) {
                        Log.i(TAG, "읽은 책 순: ${it}")
                        bookListAdapter.updataList(it)
                        if (it.size == 0) { showDataEmptyScreen() } else { hideDataEmptyScreen() }
                        Log.i(TAG, "현재 읽은 후 책 갯수: ${it.size}")
                    }
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
                        if (beforeReadBookList.value == null || beforeReadBookList.value?.size == 0) {
                            mainViewModel.showDataEmptyScreen()
                        } else {
                            mainViewModel.hideDataEmptyScreen()
                            bookListAdapter.updataList(beforeReadBookList.value!!)
                        }
                        binding.yearPickerLayout.visibility = View.GONE
                    }
                    getString(R.string.reading) -> {
                        if(readingBookList.value == null || readingBookList.value?.size == 0) {
                            mainViewModel.showDataEmptyScreen()
                        } else {
                            mainViewModel.hideDataEmptyScreen()
                            bookListAdapter.updataList(readingBookList.value!!)
                        }
                        binding.yearPickerLayout.visibility = View.GONE
                    }
                    getString(R.string.end_read) -> {
                        if(endReadBookList.value == null || endReadBookList.value?.size == 0) {
                            mainViewModel.showDataEmptyScreen()
                        } else {
                            mainViewModel.hideDataEmptyScreen()
                            bookListAdapter.updataList(endReadBookList.value!!)
                        }
                        binding.yearPickerLayout.visibility = View.VISIBLE
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
                var intent = Intent(binding.root.context, BookActivity::class.java)
                when (mainViewModel._selectedMenu.value) {
                    binding.root.context.getString(R.string.before_read) -> {
                        Log.i(TAG, "읽을 책: " + mainViewModel.beforeReadBookList.value?.get(position)?.idx)
                        intent.putExtra("idx", mainViewModel.beforeReadBookList.value?.get(position)?.idx)
                        intent.putExtra("reading_state", MainViewModel.BEFORE_READ)
                    }
                    binding.root.context.getString(R.string.reading) -> {
                        Log.i(TAG, "읽는 중: " + mainViewModel.readingBookList.value?.get(position)?.idx)
                        intent.putExtra("idx", mainViewModel.readingBookList.value?.get(position)?.idx)
                        intent.putExtra("reading_state", MainViewModel.READING)
                    }
                    binding.root.context.getString(R.string.end_read) -> {
                        Log.i(TAG, "읽은 책: " + mainViewModel.endReadBookList.value?.get(position)?.idx)
                        intent.putExtra("idx", mainViewModel.endReadBookList.value?.get(position)?.idx)
                        intent.putExtra("reading_state", MainViewModel.END_READ)
                    }
                }
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.fade_in,R.anim.not_move_activity)
            }
        })
        binding.yearPickerLayout.setOnClickListener {
            val dialog = AlertDialog.Builder(context).create()

            val edialog : LayoutInflater = LayoutInflater.from(context)
            val mView : View = edialog.inflate(R.layout.year_picker_dialog,null)

            val year : NumberPicker = mView.findViewById(R.id.yearpicker_datepicker)
//            val month : NumberPicker = mView.findViewById(R.id.monthpicker_datepicker)
            val cancel : Button = mView.findViewById(R.id.dialog_cancel)
            val save : Button = mView.findViewById(R.id.dialog_confirmation)


            //  순환 안되게 막기
            year.wrapSelectorWheel = false

            //  editText 설정 해제
            year.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            //  최소값 설정
            year.minValue = 1950

            //  최대값 설정
            year.maxValue = 2200

            year.value = mainViewModel.pickerYear.value.toString().toInt()

            //  취소 버튼 클릭 시
            cancel.setOnClickListener {
                dialog.dismiss()
                dialog.cancel()
            }

            //  완료 버튼 클릭 시
            save.setOnClickListener {
                binding.tvYear.text = year.value.toString()
                mainViewModel.pickerYear.value = year.value.toString()
                mainViewModel.searchByCategory(year.value.toString()) // Room 쿼리 변경 후 리스트 갱신

                dialog.dismiss()
                dialog.cancel()
            }

            dialog.setView(mView)
            dialog.create()

            dialog.show()
        }
    }

}