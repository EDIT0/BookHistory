package com.ejstudio.bookhistory.presentation.view.activity.main.booklist


import android.Manifest
import android.R.attr
import android.R.attr.path
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityBookBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.adapter.main.booklist.BookMemoViewPagerFragmentAdapter
import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookInfoBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookMenuChangeBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.BookViewModel
import com.ejstudio.bookhistory.util.UserInfo
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*
import android.R.attr.orientation

import android.R.attr.bitmap
import android.graphics.Matrix
import android.os.Environment
import android.R.id
import java.lang.Exception
import android.R.attr.data
import com.theartofdev.edmodo.cropper.CropImage
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import android.R.attr.data





class BookActivity : BaseActivity<ActivityBookBinding>(R.layout.activity_book) {

    private val TAG: String? = BookActivity::class.java.simpleName
    public val bookViewModel: BookViewModel by viewModel()
    private lateinit var bookMemoViewPagerFragmentAdapter: BookMemoViewPagerFragmentAdapter
    lateinit var deleteDialog: Dialog
    private lateinit var bookInfoBottomSheetDialogFragment: BookInfoBottomSheetDialogFragment
    private lateinit var bookMenuChangeBottomSheetDialogFragment: BookMenuChangeBottomSheetDialogFragment

    var photoURI: Uri? = null // 카메라 원본이미지 Uri를 저장할 변수
    val FLAG_REQ_CAMERA = 200
    var CROP_FROM_CAMERA = 111

    var exif: ExifInterface? = null

    lateinit var deleteFile: File

    object deleteImagePath{
        var path = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.bookViewModel = bookViewModel

        recvIntent()
        viewModelCallback()
        viewPagerAndTabLayoutSetting()

//        val dir: File = File(Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/Pictures", "/BookHistory")
//        if (!dir.exists()) {
//            dir.mkdirs()
//        }
    }

    fun recvIntent() {
        bookViewModel.book_idx = intent.getIntExtra("idx", 0)
        bookViewModel.reading_state = intent.extras?.getString("reading_state")!!
        Log.i(TAG, "책 idx: " + bookViewModel.book_idx + " 책 메뉴상태: " + bookViewModel.reading_state)
        bookViewModel.getIdxBookInfo()
        bookViewModel.getIdxImageMemo()
    }

    fun viewModelCallback() {
        with(bookViewModel) {
            backButton.observe(this@BookActivity, Observer {
                activityBackButton()
            })
            bookInfo.observe(this@BookActivity, Observer {
                if(it != null) {
                    Log.i(TAG, "현재 선택한 책: " + it.title + " ${it.thumbnail}")
                    bookThumbnail = it.thumbnail?:""
                    bookTitle.value = it.title
                    bookAuthors.value = it.authors
                    Glide.with(binding.root.context)
                        .load(it.thumbnail)
                        .error(R.drawable.img_bookcover_null)
                        .into(binding.bookInfoBackgroundView)
                    bookPublisher = it.publisher?:""
                    bookDatetime = it.datetime?:""
                    bookContents = it.contents?:""
                    bookUrl = it.url?:""
                    bookReadingState = it.reading_state?:""
                    _selectedMenu.value = bookReadingState
                }
            })
            deleteBook.observe(this@BookActivity, Observer {
                deleteDialog = Dialog(binding.root.context);       // Dialog 초기화
//                deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
                deleteDialog.setContentView(R.layout.dialog_delete_idx_book_info);
                deleteDialog.findViewById<TextView>(R.id.dialog_tv_title).setText("책을 삭제하면\n글/그림 기록도 삭제됩니다.")
                deleteDialog.findViewById<TextView>(R.id.dialog_tv_subTitle).setText("지운 데이터는 복구가 불가능합니다.")
                showDeleteDialog()
            })
            showBookInfo.observe(this@BookActivity, Observer {
                bookInfoBottomSheetDialogFragment = BookInfoBottomSheetDialogFragment()
                bookInfoBottomSheetDialogFragment.show(supportFragmentManager, "tag")
            })
            contentsSeeDetail.observe(this@BookActivity, Observer {
                goToContentsSeeDetail()
            })
            changeMenu.observe(this@BookActivity, Observer {
                bookMenuChangeBottomSheetDialogFragment = BookMenuChangeBottomSheetDialogFragment()
                val bundle = Bundle()
                Log.i(TAG, "현재 상태는? " + bookReadingState)
                bundle.putString("state", bookReadingState)
                bookMenuChangeBottomSheetDialogFragment.setArguments(bundle)
                bookMenuChangeBottomSheetDialogFragment.show(supportFragmentManager, "tag")
            })
            selectedMenu.observe(this@BookActivity, Observer {
                Log.i(TAG, "메뉴 변경 $it")
                when (it) {
                    getString(R.string.before_read) -> {
                        bookReadingState = getString(R.string.before_read)
                    }
                    getString(R.string.reading) -> {
                        bookReadingState = getString(R.string.reading)
                    }
                    getString(R.string.end_read) -> {
                        bookReadingState = getString(R.string.end_read)
                    }
                }

            })
            clickFloaing.observe(this@BookActivity, Observer {
                if(currentTab.equals(BookViewModel.TEXT)) {
                    goToWriteTextMemoActivity()
                } else if(currentTab.equals(BookViewModel.IMAGE)) {
                    goToCamera()
                }
            })
        }
    }

    fun viewPagerAndTabLayoutSetting() {
        // 2) FragmentStateAdapter 생성 : Fragment 여러개를 ViewPager2에 연결해주는 역할
        bookMemoViewPagerFragmentAdapter = BookMemoViewPagerFragmentAdapter(this)

        // 3) ViewPager2의 adapter에 설정
        binding.viewPager2Container.adapter = bookMemoViewPagerFragmentAdapter

        // ###### TabLayout과 ViewPager2를 연결
        // 2. TabLayout과 ViewPager2를 연결하고, TabItem의 메뉴명을 설정한다.
        TabLayoutMediator(binding.tabLayout, binding.viewPager2Container, {tab, position ->
            if(position == 0) {
                tab.setIcon(R.drawable.ic_notes_black_24dp)
                tab.id = position // 각 탭 구별을 위한 아이디 부여
            }
            else if(position == 1) {
                tab.setIcon(R.drawable.ic_image_24dp)
                tab.id = position // 각 탭 구별을 위한 아이디 부여
            }

        }).attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // 탭이 선택 되었을 때
                Log.i(TAG ,"선택된 탭: ${tab?.id} 0: 텍스트, 1: 이미지")
                if(tab?.id == 0) {
                    if(bookViewModel.textMemoList.value?.size == 0) {
                        bookViewModel.showDataEmptyScreen()
                    } else {
                        bookViewModel.hideDataEmptyScreen()
                    }

                    bookViewModel.currentTab = BookViewModel.TEXT

                } else if(tab?.id == 1) {
                    if(bookViewModel.imageMemoList.value?.size == 0) {
                        bookViewModel.showDataEmptyScreen()
                    } else {
                        bookViewModel.hideDataEmptyScreen()
                    }

                    bookViewModel.currentTab = BookViewModel.IMAGE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 탭이 선택되지 않은 상태로 변경 되었을 때
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 이미 선택된 탭이 다시 선택 되었을 때
            }
        })
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
            bookViewModel.deleteIdxBookInfo()
            deleteDialog.dismiss()
        }
    }

    fun goToContentsSeeDetail() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(bookViewModel.bookUrl))
        startActivity(browserIntent)
    }

    fun goToWriteTextMemoActivity() {
        var intent = Intent(this, WriteTextMemoActivity::class.java)
        intent.putExtra("book_idx", bookViewModel.book_idx)
        intent.putExtra("bookTitle", bookViewModel.bookTitle.value)
        startActivity(intent)
        overridePendingTransition(R.anim.rightin_activity,R.anim.not_move_activity)
    }

    fun goToCamera() {
        checkPermissions()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        /*
        * 사진 -> 크롭 과정 중 남는 이미지 삭제
        * */
        if (deleteImagePath.path.length >0){
            Log.i(TAG, "크롭취소 ${deleteImagePath.path}")
            deleteFile = File(deleteImagePath.path)
            deleteFile.delete()
        }

        /*
        * 크롭 후 requestCode
        * */
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                photoURI = result.uri
                Log.i(TAG, "값은? " + photoURI)

                deleteFile = File(deleteImagePath.path) // 3. 크롭 하기 전 이미지 파일 삭제
                deleteFile.delete()

//                deleteImagePath.path = "/data/data/com.example.appname/cache/" // 내부저장소 캐시
                deleteImagePath.path = photoURI.toString().substring(7, photoURI.toString().length)
                Log.i(TAG, "값은? " + photoURI.toString().substring(7, photoURI.toString().length))
//                deleteImagePath.path = absolutelyPath(photoURI!!)

                val file = File(deleteImagePath.path)
                var fileName = file.getName()
                fileName = "${UserInfo.email}.png"

                Log.i(TAG, "photoURI: " + photoURI)
                Log.i(TAG, "이미지 경로: " + deleteImagePath.path)
                Log.i(TAG, "이미지 제목 ${fileName}")

                bookViewModel.insertImageMemo(file, fileName)
                photoURI = null // 사용 후 null 처리
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }

        Log.i(TAG, "onActivityResult() 호출")

        /*
        * 사진찍기
        * */
        if(resultCode == Activity.RESULT_OK) {
            when(requestCode){
                FLAG_REQ_CAMERA -> {
                    if (photoURI != null) {
                        Log.i(TAG, "이미지 URI: ${photoURI}")
                        val bitmap = loadBitmapFromMediaStoreBy(photoURI!!)
//                        binding.textImage.setImageBitmap(bitmap)
                        Log.i(TAG, "이미지 비트맵: ${bitmap}")

                        deleteImagePath.path = absolutelyPath(photoURI!!) // 파일 경로 얻기

//                        soundDoodleInit("")

                        /*
                        * 사진 각도 맞춤
                        * */
                        try {
                            exif = ExifInterface(deleteImagePath.path)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        val orientation = exif!!.getAttributeInt(ExifInterface.ORIENTATION_ROTATE_90.toString(), ExifInterface.ORIENTATION_UNDEFINED)
                        val bmRotated: Bitmap = rotateBitmap(bitmap!!, orientation)!!

                        photoURI = getBitmapToUri(bmRotated) // bitmap에서 uri로 변경
                        deleteFile = File(deleteImagePath.path) // 1. 사진 찍으면 생성된 이미지 파일 삭제
                        deleteFile.delete()
                        deleteImagePath.path = absolutelyPath(photoURI!!)
//                        val resizedBmp = Bitmap.createScaledBitmap(bitmap!!, bitmap.width / 5, bitmap.height/5, true)
//                        photoURI = getImageUri(resizedBmp)
                        // 사진 리사이징
                        var resizeBitmap = resize(this, photoURI!!, 500)
                        photoURI = getBitmapToUri(resizeBitmap!!)
                        deleteFile = File(deleteImagePath.path) // 2. 각도 조절한 이미지 파일 삭제
                        deleteFile.delete()
                        deleteImagePath.path = absolutelyPath(photoURI!!)

//                        val file = File(deleteImagePath.path)
//                        var fileName = file.getName()
//                        fileName = "${UserInfo.email}.png"
//
//                        Log.i(TAG,"photoURI: "+photoURI)
//                        Log.i(TAG,"이미지 경로: "+ deleteImagePath.path)
//                        Log.i(TAG,"이미지 제목 ${fileName}")
//
//                        bookViewModel.insertImageMemo(file, fileName)



                        /*
                        * 크롭 호출
                        * */
                        CropImage.activity(photoURI)
                            .start(this)

                        // 3. 리사이즈한 이미지는 이미지 저장 완료 후 삭제

//                        Observable.fromCallable {
//                            var intent = Intent("com.android.camera.action.CROP")
//                            intent.setDataAndType(photoURI, "image/*")
//                        intent.putExtra("crop", "true");
//                        // indicate aspect of desired crop
//                        intent.putExtra("aspectX", 1);
//                        intent.putExtra("aspectY", 1);
//                        // indicate output X and Y
//                        intent.putExtra("outputX", 300);
//                        intent.putExtra("outputY", 300);
////                            intent.putExtra("scale", true)
//                            intent.putExtra("return-data", true);
//                            startActivityForResult(intent, CROP_FROM_CAMERA); // CROP_FROM_CAMERA case문 이동
//                        }
//                            .subscribeOn(Schedulers.newThread())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe {
//                                Log.i(TAG, "끝이다.")
//                            }


//                        deleteFile = File(deleteImagePath.path)
//                        deleteFile.delete()

                    }
                }

                /*
                * 크롭 코드 였던 것..
                * */
                CROP_FROM_CAMERA -> {
                    if(data?.extras != null) {
                        var extras: Bundle = data?.extras!!
                        Log.i(TAG, "크롭 데이터: ${extras.get("data")}")

                        photoURI = getBitmapToUri(extras.get("data") as Bitmap) // bitmap에서 uri로 변경
                        deleteFile = File(deleteImagePath.path) // 3. 크롭 하기 전 이미지 파일 삭제
                        deleteFile.delete()
                        deleteImagePath.path = absolutelyPath(photoURI!!)

                        val file = File(deleteImagePath.path)
                        var fileName = file.getName()
                        fileName = "${UserInfo.email}.png"

                        Log.i(TAG, "photoURI: " + photoURI)
                        Log.i(TAG, "이미지 경로: " + deleteImagePath.path)
                        Log.i(TAG, "이미지 제목 ${fileName}")

                        bookViewModel.insertImageMemo(file, fileName)
                        photoURI = null // 사용 후 null 처리
                    } else {
                        Log.i(TAG, "크롭 데이터 null")
                    }
                }
            }
        }
    }

    var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() { // 권한 허가시 실행 할 내용
            initView()
        }
        override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
            // 권한 거부시 실행  할 내용
            showToast("권한 허용을 하지 않으면 서비스를 이용할 수 없습니다.")
        }
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) { // 마시멜로(안드로이드 6.0) 이상 권한 체크
            TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("앱을 이용하기 위해서는 접근 권한이 필요합니다")
                .setDeniedMessage("앱에서 요구하는 권한설정이 필요합니다.\n [설정] > [권한] 에서 사용으로 활성화해주세요.")
                .setPermissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ).check()
        } else {
            initView()
        }
    }

    fun initView() {
        // 카메라 인텐트 생성
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        createImageUri("imagefolder"+ Calendar.getInstance().getTime(), "image/jpeg")?.let { uri ->
            photoURI = uri
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            Log.i(TAG, "카메라 이미지 uri ${photoURI}")
            startActivityForResult(takePictureIntent, FLAG_REQ_CAMERA)
        }
    }

    fun createImageUri(filename: String, mimeType: String) : Uri? {
        var values = ContentValues()
//        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
//        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    fun loadBitmapFromMediaStoreBy(photoUri: Uri): Bitmap? {
        var image: Bitmap? = null
        try {
            image = if (Build.VERSION.SDK_INT > 27) { // Api 버전별 이미지 처리
                val source: ImageDecoder.Source = ImageDecoder.createSource(this.contentResolver, photoUri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }

    fun absolutelyPath(path: Uri): String {

        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = contentResolver.query(path, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }

    private fun getBitmapToUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null)
        return Uri.parse(path)
    }

    private fun resize(context: Context, uri: Uri, resize: Int): Bitmap? {
        var resizeBitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        try {
            BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(uri),
                null,
                options
            ) // 1번
            var width = options.outWidth
            var height = options.outHeight
            var samplesize = 1
            while (true) { //2번
                if (width / 2 < resize || height / 2 < resize) break
                width /= 2
                height /= 2
                samplesize *= 2
            }
            options.inSampleSize = samplesize
            val bitmap = BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(uri),
                null,
                options
            ) //3번
            resizeBitmap = bitmap
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return resizeBitmap
    }

    fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap? {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> return bitmap
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
            else -> return bitmap
        }
        return try {
            val bmRotated =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()
            bmRotated
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            null
        }
    }

    fun soundDoodleInit(path: String?) {
        val dir = File(path)
        val childFileList = dir.listFiles()
        if (dir.exists()) {
            for (childFile in childFileList) {
                if (childFile.isDirectory) {
                    soundDoodleInit(childFile.absolutePath)
                } else {
                    childFile.delete()
                }
            }
            dir.delete()
        }
    }

}