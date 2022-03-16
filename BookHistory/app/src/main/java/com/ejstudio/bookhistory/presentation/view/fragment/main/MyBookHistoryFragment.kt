package com.ejstudio.bookhistory.presentation.view.fragment.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.FragmentMyBookHistoryBinding
import com.ejstudio.bookhistory.presentation.view.activity.main.MainActivity
import com.ejstudio.bookhistory.presentation.view.activity.main.mybookhistory.CalendarClickActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import java.lang.Exception
import java.util.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.ejstudio.bookhistory.util.EventDecorator
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener


class MyBookHistoryFragment : Fragment() {

    private val TAG = MyBookHistoryFragment::class.java.simpleName

    var dotDecorator: EventDecorator? = null
    var dates: HashSet<CalendarDay> = HashSet()

    lateinit var binding: FragmentMyBookHistoryBinding
//    private val mainViewModel: MainViewModel by viewModel()
    lateinit var mainViewModel: MainViewModel

    var year: Int = 0
    var month: Int = 0
    var day: Int = 0

//    private lateinit var calendarClickBottomSheetDialogFragment: CalendarClickBottomSheetDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_book_history, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        mainViewModel = (activity as MainActivity).mainViewModel
        binding.mainViewModel = mainViewModel
        Log.i(TAG, "메인 뷰 모델 값 ${mainViewModel.hashCode()}")


//        dates.add(CalendarDay.today()); // 오늘 날짜 밑에 dot 출력
//        dates.add(CalendarDay.from(2021, 10, 15)); // 2021년 10월 15일 밑에 dot 출력

//        binding.calendarView.setSelectedDate(CalendarDay.today())
//        binding.calendarView.clearSelection()

//        dotDecorator = EventDecorator(binding.root.context, Color.parseColor("#ffa967"), dates) // dot 데코 클래스
//        binding.calendarView.addDecorator(dotDecorator)

//        binding.calendarView.getSe

        calendarListener()
        viewModelCallback()

        return binding.root
    }

    fun calendarListener() {
        binding.calendarView.setOnMonthChangedListener(object : OnMonthChangedListener {
            override fun onMonthChanged(widget: MaterialCalendarView?, date: CalendarDay?) {
                Log.i(TAG, "onMonthChanged: " + date?.year + " / calendarYear: ${mainViewModel.calendarYear.value.toString()}")
                if(!date?.year.toString().equals(mainViewModel.calendarYear.value.toString())) {
                    Log.i(TAG, "onMonthChanged 호출?")
                    mainViewModel.calendarYear.value = date?.year.toString()
                }
            }
        })


        binding.calendarView.setOnDateChangedListener(object : OnDateSelectedListener {

            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                binding.calendarView.clearSelection()

//                calendarClickBottomSheetDialogFragment = CalendarClickBottomSheetDialogFragment()
                var year = "${date.year}"
                var month = "${date.month}"
                var day = "${date.day}"
                if(month.length == 1) {
                    month = "0" + month
                }
                if(day.length == 1) {
                    day = "0" + day
                }
//                mainViewModel.getCalendarDateMemoList("${date.year}-${month}-${date.day}")
//                mainViewModel.calendarDate = "${date.year}-${month}-${date.day}"
//                try{
//                    Log.i(TAG, "${mainViewModel.calendarDate} 다이얼로그 호출 때 사이즈: " + mainViewModel.calendarDateMemoList.value?.size)
//                } catch (e: Exception) {}
                val bundle = Bundle()
                Log.i(TAG, "클릭한 날짜: ${date.year}-${month}-${date.day}")
//                bundle.putString("year", "${date.year}-${date.month}-${date.day}")
                bundle.putString("year", "${date.year}")
                bundle.putString("month", "${month}")
                bundle.putString("day", "${date.day}")

                goToCalendarClickActivity(year, month, day)
//                calendarClickBottomSheetDialogFragment.setArguments(bundle)
//                calendarClickBottomSheetDialogFragment.show(requireActivity().supportFragmentManager, "tag")
            }
        })
    }

    fun viewModelCallback() {
        with(mainViewModel) {
            totalTextImageMemoList.observe(viewLifecycleOwner, Observer {
                try {
                    if(dates.size != 0) {
                        dates.clear()
                        binding.calendarView.removeDecorators()
                    }
                    Log.i(TAG, "사이즈: " + it.size)

                    var record_date = ""

                    Thread(Runnable {
                        for(i in 0 until it.size) {
                            Log.i(TAG, "내 기록들: ${it.get(i).memo_contents}")
                            Log.i(TAG, "책 이름: ${it.get(i).title}")

                            var date = it.get(i).save_datetime.substring(0, 10)

                            if(record_date.equals(date)) {
                                continue
                            }

//                        Log.i(TAG, "1. date: ${date}")
                            val idx: Int = date.indexOf("-")
//                        Log.i(TAG, "2. idx: ${idx}")
                            year = date.substring(0, idx).toInt()
//                        Log.i(TAG, "3. year: ${year}")
                            month = date.substring(idx+1, idx+3).toInt()
//                        Log.i(TAG, "4. month: ${month}")
                            day = date.substring(idx+4, idx+6).toInt()
//                        Log.i(TAG, "5. day: ${day}")


                            dates.add(CalendarDay.from(year, month, day))

                            Log.i(TAG, "년: ${year} 월: ${month} 일: ${day}")

                            record_date = date

                        }
                        requireActivity().runOnUiThread {
                            dotDecorator = EventDecorator(binding.root.context, Color.parseColor("#ffa967"), dates) // dot 데코 클래스
                            binding.calendarView.addDecorator(dotDecorator)
                        }
                    }).start()


                }catch (e: Exception) {
                    Log.i(TAG, e.message.toString())
                }
            })
        }
    }

    // 현재 Year
    fun getCurrentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

    // 현재 Month
    fun getCurrentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1

    // 현재 Day
    fun getCurrentDay(): Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    fun goToCalendarClickActivity(year: String, month: String, day: String) {
        var intent = Intent(binding.root.context, CalendarClickActivity::class.java)
        intent.putExtra("year", year)
        intent.putExtra("month", month)
        intent.putExtra("day", day)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.bottomin_activity, R.anim.not_move_activity)
    }
}