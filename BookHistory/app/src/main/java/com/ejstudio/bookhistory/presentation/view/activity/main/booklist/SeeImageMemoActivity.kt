package com.ejstudio.bookhistory.presentation.view.activity.main.booklist

import android.Manifest
import android.R.attr
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityBookBinding
import com.ejstudio.bookhistory.databinding.ActivitySeeImageMemoBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.BookViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.SeeImageMemoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.content.Intent

import android.R.attr.path
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.lifecycleScope
import com.ejstudio.bookhistory.presentation.view.viewmodel.login.FindPasswordViewModel
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList


class SeeImageMemoActivity : BaseActivity<ActivitySeeImageMemoBinding>(R.layout.activity_see_image_memo) {

    private val TAG: String? = SeeImageMemoActivity::class.java.simpleName
    public val seeImageMemoViewModel: SeeImageMemoViewModel by viewModel()
    lateinit var deleteDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.seeImageMemoViewModel = seeImageMemoViewModel

        recvIntent()
        viewModelCallback()
        buttonClickListener()
        binding.imageView.setImageResource(R.drawable.img_bookcover_null);
    }

    fun recvIntent() {
        seeImageMemoViewModel.imageMemoIdx = intent.getIntExtra("imageMemoIdx", 0)
        seeImageMemoViewModel.imageUrl.value = intent.getStringExtra("imageUrl")
        seeImageMemoViewModel.bookTitle = intent.getStringExtra("bookTitle")!!
        Log.i(TAG, "책 idx: " + seeImageMemoViewModel.imageMemoIdx + " / " + seeImageMemoViewModel.imageUrl.value.toString())
    }

    fun viewModelCallback() {
        with(seeImageMemoViewModel) {
            backButton.observe(this@SeeImageMemoActivity, Observer {
                activityBackButton()
            })
            imageUrl.observe(this@SeeImageMemoActivity, Observer {
                Glide.with(binding.root)
                    .load(imageUrl.value.toString())
                    .into(binding.imageView)
            })
            deleteImageMemo.observe(this@SeeImageMemoActivity, Observer {
                deleteDialog = Dialog(binding.root.context);       // Dialog 초기화
//                deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
                deleteDialog.setContentView(R.layout.dialog_delete_idx_book_info);
                deleteDialog.findViewById<TextView>(R.id.dialog_tv_subTitle).setText("사진을 삭제하시겠습니까?")
                deleteDialog.findViewById<TextView>(R.id.dialog_tv_title).visibility = View.INVISIBLE
                showDeleteDialog()
            })
            requestSnackbar.observe(this@SeeImageMemoActivity, Observer {
                when(snackbarMessage) {
                    SeeImageMemoViewModel.MessageSet.NETWORK_NOT_CONNECTED.toString() -> {
                        snackbarMessage = getString(R.string.NETWORK_NOT_CONNECTED)
                    }
                }
                showSnackbar(snackbarMessage)
            })
        }
    }

    fun activityBackButton() {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.not_move_activity, R.anim.fade_out)
    }

    fun showDeleteDialog() {
        deleteDialog.show()

        val dialog_cancel: Button = deleteDialog.findViewById(R.id.dialog_cancel)
        dialog_cancel.setOnClickListener {
            deleteDialog.dismiss()
        }

        val dialog_confirmation: Button = deleteDialog.findViewById(R.id.dialog_confirmation)
        dialog_confirmation.setOnClickListener {
            seeImageMemoViewModel.deleteIdxImageMemo()
            deleteDialog.dismiss()
        }
    }

    fun buttonClickListener() {
        binding.ibShareButton.setOnClickListener {
//            val sharingIntent = Intent(Intent.ACTION_SEND)
//            val screenshotUri: Uri = Uri.parse(seeImageMemoViewModel.imageUrl.value.toString()) // android image path
//
//            sharingIntent.type = "image/png"
//            sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri)
//            startActivity(Intent.createChooser(sharingIntent, "Share image using"))

            checkPermissions()


        }
    }

    fun convertBitmapFromURL(url: String) : Bitmap? {
        try{
            val url = URL(url)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(input)
            return bitmap

        }catch (e: Exception) {}
        return null
    }

    var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() { // 권한 허가시 실행 할 내용
            initView()
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            // 권한 거부시 실행  할 내용
            showToast("권한 허용을 하지 않으면 서비스를 이용할 수 없습니다.")
        }
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) { // 마시멜로(안드로이드 6.0) 이상 권한 체크
            TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("앱을 이용하기 위해서는 접근 권한이 필요합니다")
                .setDeniedMessage("앱에서 요구하는 권한설정이 필요합니다.\n [설정] > [권한] 에서 사용으로 활성화해주세요.")
                .setPermissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ).check()
        } else {
            initView()
        }
    }

    fun initView() {
        if(!seeImageMemoViewModel.checkNetworkState()) return
        lifecycleScope.launch(Dispatchers.IO) {
            val bitmap = convertBitmapFromURL(seeImageMemoViewModel.imageUrl.value.toString())
            val bitmapPath = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
            val bitmapUri = Uri.parse(bitmapPath)
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "image/png"
            startActivity(intent)
        }
    }
}