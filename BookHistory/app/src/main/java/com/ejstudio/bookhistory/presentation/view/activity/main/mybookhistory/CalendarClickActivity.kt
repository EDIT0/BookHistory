package com.ejstudio.bookhistory.presentation.view.activity.main.mybookhistory

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityBookBinding
import com.ejstudio.bookhistory.databinding.ActivityCalendarClickBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.activity.main.booklist.BookActivity
import com.ejstudio.bookhistory.presentation.view.activity.main.booklist.SeeImageMemoActivity
import com.ejstudio.bookhistory.presentation.view.activity.main.booklist.SeeTextMemoActivity
import com.ejstudio.bookhistory.presentation.view.adapter.main.booklist.BookMemoViewPagerFragmentAdapter
import com.ejstudio.bookhistory.presentation.view.adapter.main.mybookhistory.CalendarDateMemoAdapter
import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookInfoBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookMenuChangeBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.BookViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.mybookhistory.CalendarClickViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ejstudio.bookhistory.util.Converter


class CalendarClickActivity : BaseActivity<ActivityCalendarClickBinding>(R.layout.activity_calendar_click) {

    private val TAG: String? = CalendarClickActivity::class.java.simpleName
    public val calendarClickViewModel: CalendarClickViewModel by viewModel()
    private var calendarDateMemoAdapter: CalendarDateMemoAdapter = CalendarDateMemoAdapter()

    var pressedX = 0f

    lateinit var dataEmptyView: View
    lateinit var emptyImage: ImageView
    lateinit var emptyTextTitle: TextView
    lateinit var emptyTextSubTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_calendar_click)

        binding.calendarClickViewModel = calendarClickViewModel

        recvIntent()
        dataEmptyScreenSetting()
        viewModelCallback()
        settingRecyclerView()
        buttonClickListener()

        binding.topView.setOnTouchListener { view, motionEvent ->
            var distance = 0f

            when (motionEvent.getAction()) {
                MotionEvent.ACTION_DOWN ->
                    // 손가락을 touch 했을 떄 x 좌표값 저장
                    pressedX = motionEvent.getY()
                MotionEvent.ACTION_UP ->
                    // 손가락을 떼었을 때 저장해놓은 x좌표와의 거리 비교
                    distance = pressedX - motionEvent.getY()
            }
            // 해당 거리가 100이 되지 않으면 이벤트 처리 하지 않는다.
            // 해당 거리가 100이 되지 않으면 이벤트 처리 하지 않는다.
            if (Math.abs(distance) < 200) {
                false
            } else {
                onBackPressed()
            }

            if (distance > 0) {
                // 손가락을 왼쪽으로 움직였으면 오른쪽 화면이 나타나야 한다.

            } else {

//                // 손가락을 오른쪽으로 움직였으면 왼쪽 화면이 나타나야 한다.
//                val intent = Intent(mContext, LeftActivity::class.java)
//                startActivity(intent)
            }

//            finish() // finish 해주지 않으면 activity가 계속 쌓인다.


            true
        }
    }

    fun recvIntent() {
        calendarClickViewModel.calendarDate = "" +
                intent.getStringExtra("year") +
                "-" + intent.getStringExtra("month") +
                "-" + intent.getStringExtra("day")
        calendarClickViewModel.getCalendarDate()

        var day = ""

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val nDate = dateFormat.parse(calendarClickViewModel.calendarDate)

        val cal = Calendar.getInstance()
        cal.time = nDate
        Log.i(TAG, "? " + nDate + " / " + calendarClickViewModel.calendarDate)

        val dayNum = cal.get(Calendar.DAY_OF_WEEK)
        when (dayNum) {
            1 -> day = "일요일"
            2 -> day = "월요일"
            3 -> day = "화요일"
            4 -> day = "수요일"
            5 -> day = "목요일"
            6 -> day = "금요일"
            7 -> day = "토요일"
        }

        binding.tvDay.setText(intent.getStringExtra("day"))
        binding.tvDateName.setText(day)
        Log.i(TAG, "가져올 메모 날짜: " + calendarClickViewModel.calendarDate)
    }

    fun dataEmptyScreenSetting() {
        dataEmptyView = binding.root.findViewById<View>(R.id.include_data_empty_screen)
        emptyImage = dataEmptyView.findViewById<ImageView>(R.id.iv_emptyImage)
        emptyTextTitle = dataEmptyView.findViewById<TextView>(R.id.tv_emptyTextTitle)
        emptyTextSubTitle = dataEmptyView.findViewById<TextView>(R.id.tv_emptyTextSubTitle)
    }

    fun viewModelCallback() {
        with(calendarClickViewModel) {
            if(calendarDateMemoList != null) {
                calendarDateMemoList.observe(this@CalendarClickActivity, Observer {
                    if (it != null && it.size != 0) {
                        hideDataEmptyScreen()
                        calendarDateMemoAdapter.updataList(it)
                    } else {
                        showDataEmptyScreen()
                        calendarDateMemoAdapter.updataList(listOf())
                    }
                })
            }
            isData.observe(this@CalendarClickActivity, Observer {
                Glide.with(binding.root)
                    .load(R.drawable.ic_sentiment_neutral_24dp)
                    .override(
                        Converter.dpToPx(binding.root.context, 24),
                        Converter.dpToPx(binding.root.context, 24))
                    .into(emptyImage)
                emptyTextTitle.text = "오늘은\n쉬어가는 날~"
            })
            backButton.observe(this@CalendarClickActivity, Observer {
                activityBackButton()
            })
        }
    }

    fun settingRecyclerView() {
        var layoutmanager = LinearLayoutManager(binding.rcDateTextImageMemo.context)
        binding.rcDateTextImageMemo.layoutManager = layoutmanager
        binding.rcDateTextImageMemo.adapter = calendarDateMemoAdapter
    }

    fun buttonClickListener() {
        calendarDateMemoAdapter.setOnOnImageMemoClickListener(object : CalendarDateMemoAdapter.OnImageMemoClickListener{
            override fun onItemClick(holder: CalendarDateMemoAdapter.ImageMemoViewHolder?, view: View?, position: Int) {
                goToSeeImageMemoActivity(position)
            }
        })

        calendarDateMemoAdapter.setOnTextMemoClickListener(object : CalendarDateMemoAdapter.OnTextMemoClickListener{
            override fun onItemClick(holder: CalendarDateMemoAdapter.TextMemoViewHolder?, view: View?, position: Int) {
                goToSeeTextMemoActivity(position)
            }

        })
    }

    fun goToSeeImageMemoActivity(position: Int) {
        var intent = Intent(binding.root.context, SeeImageMemoActivity::class.java)
        intent.putExtra("imageMemoIdx", calendarClickViewModel.calendarDateMemoList.value?.get(position)?.idx)
        intent.putExtra("imageUrl", calendarClickViewModel.calendarDateMemoList.value?.get(position)?.memo_contents)
        intent.putExtra("bookTitle", calendarClickViewModel.calendarDateMemoList.value?.get(position)?.title?.trim())
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.not_move_activity)
    }

    fun goToSeeTextMemoActivity(position: Int) {
        var intent = Intent(binding.root.context, SeeTextMemoActivity::class.java)
        intent.putExtra("textMemoIdx", calendarClickViewModel.calendarDateMemoList.value?.get(position)?.idx)
        intent.putExtra("bookTitle", calendarClickViewModel.calendarDateMemoList.value?.get(position)?.title?.trim())
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in,R.anim.not_move_activity)
    }

    fun activityBackButton() {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.not_move_activity, R.anim.bottomout_activity)
    }
}