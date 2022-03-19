package com.ejstudio.bookhistory.presentation.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityWelcomeBinding
import com.ejstudio.bookhistory.domain.model.WelcomeModel
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.adapter.login.WelcomeAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.ejstudio.bookhistory.presentation.view.activity.login.LoginActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.WelcomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>(R.layout.activity_welcome) {

    private val TAG: String = WelcomeActivity::class.java.getSimpleName()

//    private val MIN_SCALE = 0.85f // 뷰가 몇퍼센트로 줄어들 것인지
//    private val MIN_ALPHA = 0.5f // 어두워지는 정도를 나타낸 듯 하다.
    private val MIN_SCALE = 0.75f

    private val welcomeViewModel: WelcomeViewModel by viewModel()

    var items = ArrayList<WelcomeModel>()
    lateinit var welcomeAdapter : WelcomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        settingImg()
        settingWelcomeAdapterAndViewPager2()
        buttonClickListener()
    }

    fun settingImg() {
        items.add(WelcomeModel(R.drawable.img_welcome1))
        items.add(WelcomeModel(R.drawable.img_welcome2))
        items.add(WelcomeModel(R.drawable.img_welcome3))
    }

    fun settingWelcomeAdapterAndViewPager2() {
        welcomeAdapter = WelcomeAdapter(items)

        binding.vpGuideImage.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL)
        binding.vpGuideImage.setOffscreenPageLimit(3)
        binding.vpGuideImage.adapter = welcomeAdapter
        binding.wormDotsIndicator.setViewPager2(binding.vpGuideImage)
        binding.vpGuideImage.setPageTransformer(DepthPageTransformer()) // 애니메이션 적용

        binding.vpGuideImage.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == items.size - 1) {
                    binding.btnNext.text = "시작하기"
                } else {
                    binding.btnNext.text = "다음"
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
    }

    fun buttonClickListener() {
        binding.btnNext.setOnClickListener {
            if(binding.btnNext.text.equals("다음")) {
                binding.vpGuideImage.setCurrentItem(binding.vpGuideImage.currentItem + 1,true)
            } else if(binding.btnNext.text.equals("시작하기")) {
                welcomeViewModel.changeIsFirstWelcome()

                val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    /* 공식문서에 있는 코드 긁어온거임 */
//    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {
//        override fun transformPage(view: View, position: Float) {
//            view.apply {
//                val pageWidth = width
//                val pageHeight = height
//                when {
//                    position < -1 -> { // [-Infinity,-1)
//                        // This page is way off-screen to the left.
//                        alpha = 0f
//                    }
//                    position <= 1 -> { // [-1,1]
//                        // Modify the default slide transition to shrink the page as well
//                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
//                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
//                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
//                        translationX = if (position < 0) {
//                            horzMargin - vertMargin / 2
//                        } else {
//                            horzMargin + vertMargin / 2
//                        }
//
//                        // Scale the page down (between MIN_SCALE and 1)
//                        scaleX = scaleFactor
//                        scaleY = scaleFactor
//
//                        // Fade the page relative to its size.
//                        alpha = (MIN_ALPHA +
//                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
//                    }
//                    else -> { // (1,+Infinity]
//                        // This page is way off-screen to the right.
//                        alpha = 0f
//                    }
//                }
//            }
//        }
//    }

    @RequiresApi(21)
    inner class DepthPageTransformer : ViewPager2.PageTransformer {

        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 0 -> { // [-1,0]
                        // Use the default slide transition when moving to the left page
                        alpha = 1f
                        translationX = 0f
                        translationZ = 0f
                        scaleX = 1f
                        scaleY = 1f
                    }
                    position <= 1 -> { // (0,1]
                        // Fade the page out.
                        alpha = 1 - position

                        // Counteract the default slide transition
                        translationX = pageWidth * -position
                        // Move it behind the left page
                        translationZ = -1f

                        // Scale the page down (between MIN_SCALE and 1)
                        val scaleFactor = (MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position)))
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }
}