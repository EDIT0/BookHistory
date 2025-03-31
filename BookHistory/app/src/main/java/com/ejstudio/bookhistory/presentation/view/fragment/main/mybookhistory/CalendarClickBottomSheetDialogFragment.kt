package com.ejstudio.bookhistory.presentation.view.fragment.main.mybookhistory

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.FragmentCalendarClickBottomSheetDialogBinding
import com.ejstudio.bookhistory.presentation.view.activity.main.MainActivity
import com.ejstudio.bookhistory.presentation.view.adapter.main.mybookhistory.CalendarDateMemoAdapter
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CalendarClickBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private val TAG = CalendarClickBottomSheetDialogFragment::class.java.simpleName

    //    private val mainViewModel: MainViewModel by viewModel()
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: FragmentCalendarClickBottomSheetDialogBinding
    lateinit var root: View

    lateinit var date: String
    lateinit var year: String
    lateinit var month: String
    lateinit var day: String

    private var calendarDateMemoAdapter: CalendarDateMemoAdapter = CalendarDateMemoAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar_click_bottom_sheet_dialog, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        mainViewModel = (activity as MainActivity).mainViewModel
        binding.mainViewModel = mainViewModel
        root = binding.root
        Log.i(TAG, "메인 뷰 모델 값 ${mainViewModel.hashCode()}")

        recvIntent()
//        settingViewPager2()
        settingRecyclerView()
        buttonClickListener()




        return root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface: DialogInterface? ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog?
            setupRatio(bottomSheetDialog!!)
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = true
        behavior.skipCollapsed = true
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 85 / 100
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun recvIntent() {
        year = arguments?.getString("year")!!.trim()
        month = arguments?.getString("month")!!.trim()
        day = arguments?.getString("day")!!.trim()
        date = "${year}-${month}-${day}"
        Log.i(TAG, "date 값: ${date}")

//        if(mainViewModel._calendarDateMemoList.value?.size != 0) {
//        }
        mainViewModel.calendarDate.value = date.trim()
//        mainViewModel.getCalendarDateMemoList(mainViewModel.calendarDate)

//        mainViewModel.getCalendarDateMemoList(date.trim())
//        Log.i(TAG, "${date.trim()} 사이즈: " + mainViewModel.calendarDateMemoList?.value?.size)

    }

//    fun settingViewPager2() {
//        val from = date
//        val transFormat = SimpleDateFormat("yyyy-MM-dd")
//        val to: Date = transFormat.parse(from)
//        Log.i(TAG, "찍은 날짜는? ${to}")
//        mainViewModel.setViewPagerDateItem(60, to)
//        calendarDateMemoAdapter = CalendarDateMemoAdapter(mainViewModel.calendarDateMemoList)
//        binding.dateMemoViewPager2.setAdapter(calendarDateMemoAdapter)
//        binding.dateMemoViewPager2.setCurrentItem(59)
//    }

    fun settingRecyclerView() {
        var layoutmanager = LinearLayoutManager(binding.rcDateTextImageMemo.context)
        binding.rcDateTextImageMemo.layoutManager = layoutmanager
        binding.rcDateTextImageMemo.adapter = calendarDateMemoAdapter
    }

    fun viewModelCallback() {
        with(mainViewModel) {
//            calendarDate.observe(viewLifecycleOwner, Observer {
//                Log.i(TAG, "날짜 눌러서 호출함2")
//
//            })
//            calendarDateMemoList?.observe(viewLifecycleOwner, Observer {
//                Log.i(TAG, "변경됨2")
//                if(it != null && it.size != 0) {
//                    Log.i(TAG, "${it.size} ${it.get(0).memo_contents}")
//                }
//            })

        }
    }

    fun buttonClickListener(){

    }


    override fun onDismiss(dialog: DialogInterface) {
//        Log.i(TAG, "바텀 시트 끔 ${mainViewModel.calendarDateMemoList?.value?.size}")
        super.onDismiss(dialog)
    }
}