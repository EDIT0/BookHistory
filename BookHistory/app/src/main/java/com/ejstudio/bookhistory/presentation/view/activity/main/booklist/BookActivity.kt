package com.ejstudio.bookhistory.presentation.view.activity.main.booklist


import android.Manifest
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

import android.graphics.Matrix
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.MutableLiveData
import com.theartofdev.edmodo.cropper.CropImage


class BookActivity : BaseActivity<ActivityBookBinding>(R.layout.activity_book) {

    private val TAG: String? = BookActivity::class.java.simpleName

    companion object {
        var isNestedScrolling = MutableLiveData<Boolean>(true)
    }

    public val bookViewModel: BookViewModel by viewModel()
    private lateinit var bookMemoViewPagerFragmentAdapter: BookMemoViewPagerFragmentAdapter
    lateinit var deleteDialog: Dialog
    private lateinit var bookInfoBottomSheetDialogFragment: BookInfoBottomSheetDialogFragment
    private lateinit var bookMenuChangeBottomSheetDialogFragment: BookMenuChangeBottomSheetDialogFragment

    var photoURI: Uri? = null // ????????? ??????????????? Uri??? ????????? ??????
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

//        nestScroller = binding.nestedScrollview
        isNestedScrolling.value = true

        recvIntent()
        viewModelCallback()
        viewPagerAndTabLayoutSetting()
        montionLayoutTransitionListener()

//        val dir: File = File(Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/Pictures", "/BookHistory")
//        if (!dir.exists()) {
//            dir.mkdirs()
//        }


    }

    fun recvIntent() {
        bookViewModel.book_idx = intent.getIntExtra("idx", 0)
        bookViewModel.reading_state = intent.extras?.getString("reading_state")!!
        Log.i(TAG, "??? idx: " + bookViewModel.book_idx + " ??? ????????????: " + bookViewModel.reading_state)
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
                    Log.i(TAG, "?????? ????????? ???: " + it.title + " ${it.thumbnail}")
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
                    bookISBN = it.isbn?:""
                    _selectedMenu.value = bookReadingState
                }
            })
            deleteBook.observe(this@BookActivity, Observer {
                deleteDialog = Dialog(binding.root.context);       // Dialog ?????????
//                deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // ????????? ??????
                deleteDialog.setContentView(R.layout.dialog_delete_idx_book_info);
                deleteDialog.findViewById<TextView>(R.id.dialog_tv_title).setText("?????? ????????????\n???/?????? ????????? ???????????????.")
                deleteDialog.findViewById<TextView>(R.id.dialog_tv_subTitle).setText("?????? ???????????? ????????? ??????????????????.")
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
                Log.i(TAG, "?????? ?????????? " + bookReadingState)
                bundle.putString("state", bookReadingState)
                bookMenuChangeBottomSheetDialogFragment.setArguments(bundle)
                bookMenuChangeBottomSheetDialogFragment.show(supportFragmentManager, "tag")
            })
            selectedMenu.observe(this@BookActivity, Observer {
                Log.i(TAG, "?????? ?????? $it")
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
                if (!bookViewModel.checkNetworkState()) return@Observer
                if(currentTab.equals(BookViewModel.TEXT)) {
                    goToWriteTextMemoActivity()
                } else if(currentTab.equals(BookViewModel.IMAGE)) {
                    goToCamera()
                }
            })
            goToShopping.observe(this@BookActivity, Observer {
//                goToLibraryMapActivity()
                goToContentsSeeDetail()
            })
            requestSnackbar.observe(this@BookActivity, Observer {
                when(snackbarMessage) {
                    BookViewModel.MessageSet.NETWORK_NOT_CONNECTED.toString() -> {
                        snackbarMessage = getString(R.string.NETWORK_NOT_CONNECTED)
                    }
                }
                showSnackbar(snackbarMessage)
            })
            isNestedScrolling.observe(this@BookActivity, Observer {
                Log.i(TAG, "?????? ???????????? ??????: ${it}")
                binding.nestedScrollview.isNestedScrollingEnabled = it

                if(it) {

                } else {
                    binding.motionlayout.transitionToEnd()
                }
            })
        }
    }

    fun viewPagerAndTabLayoutSetting() {
        // 2) FragmentStateAdapter ?????? : Fragment ???????????? ViewPager2??? ??????????????? ??????
        bookMemoViewPagerFragmentAdapter = BookMemoViewPagerFragmentAdapter(this)

        // 3) ViewPager2??? adapter??? ??????
        binding.viewPager2Container.adapter = bookMemoViewPagerFragmentAdapter

        // ###### TabLayout??? ViewPager2??? ??????
        // 2. TabLayout??? ViewPager2??? ????????????, TabItem??? ???????????? ????????????.
        TabLayoutMediator(binding.tabLayout, binding.viewPager2Container, {tab, position ->
            if(position == 0) {
                tab.setIcon(R.drawable.ic_notes_black_24dp)
                tab.id = position // ??? ??? ????????? ?????? ????????? ??????
            }
            else if(position == 1) {
                tab.setIcon(R.drawable.ic_image_24dp)
                tab.id = position // ??? ??? ????????? ?????? ????????? ??????
            }

        }).attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // ?????? ?????? ????????? ???
                Log.i(TAG ,"????????? ???: ${tab?.id} 0: ?????????, 1: ?????????")
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

                binding.motionlayout.transitionToEnd()
                isNestedScrolling.setValue(false)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // ?????? ???????????? ?????? ????????? ?????? ????????? ???
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // ?????? ????????? ?????? ?????? ?????? ????????? ???
                binding.motionlayout.transitionToEnd()
                isNestedScrolling.setValue(false)
            }
        })
    }

    fun montionLayoutTransitionListener() {
        binding.motionlayout.setTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
//                Log.i(TAG, "onTransitionStarted: ")
            }

            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
//                Log.i(TAG, "??????: " + progress) // start 0.0, end 1.0
                if (motionLayout?.progress!! == 0.0.toFloat()) {
                    // this is start
//                    Log.i(TAG, "onTransitionChange start")
                } else if(motionLayout?.progress!! > 0.88.toFloat()){
                    // this is end
//                    Log.i(TAG, "onTransitionChange end")

                }
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if(currentId == R.id.collapsed) {
                    Log.i(TAG, "onTransitionCompleted collapsed")
                    isNestedScrolling.setValue(false)
                } else if(currentId == R.id.expanded) {
                    Log.i(TAG, "onTransitionCompleted expanded")
                    isNestedScrolling.setValue(true)
                }
            }

            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {
                TODO("Not yet implemented")
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

    fun goToLibraryMapActivity() {
        var intent = Intent(this, LibraryMapActivity::class.java)
        intent.putExtra("book_isbn", bookViewModel.bookISBN)
        startActivity(intent)
        overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        /*
        * ?????? -> ?????? ?????? ??? ?????? ????????? ??????
        * */
        if (deleteImagePath.path.length >0){
            Log.i(TAG, "???????????? ${deleteImagePath.path}")
            deleteFile = File(deleteImagePath.path)
            deleteFile.delete()
        }

        /*
        * ?????? ??? requestCode
        * */
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                photoURI = result.uri
                Log.i(TAG, "??????? " + photoURI)

                deleteFile = File(deleteImagePath.path) // 3. ?????? ?????? ??? ????????? ?????? ??????
                deleteFile.delete()

//                deleteImagePath.path = "/data/data/com.example.appname/cache/" // ??????????????? ??????
                deleteImagePath.path = photoURI.toString().substring(7, photoURI.toString().length)
                Log.i(TAG, "??????? " + photoURI.toString().substring(7, photoURI.toString().length))
//                deleteImagePath.path = absolutelyPath(photoURI!!)

                val file = File(deleteImagePath.path)
                var fileName = file.getName()
                fileName = "${UserInfo.email}.png"

                Log.i(TAG, "photoURI: " + photoURI)
                Log.i(TAG, "????????? ??????: " + deleteImagePath.path)
                Log.i(TAG, "????????? ?????? ${fileName}")

                bookViewModel.insertImageMemo(file, fileName)
                photoURI = null // ?????? ??? null ??????
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }

        Log.i(TAG, "onActivityResult() ??????")

        /*
        * ????????????
        * */
        if(resultCode == Activity.RESULT_OK) {
            when(requestCode){
                FLAG_REQ_CAMERA -> {
                    if (photoURI != null) {
                        Log.i(TAG, "????????? URI: ${photoURI}")
                        val bitmap = loadBitmapFromMediaStoreBy(photoURI!!)
//                        binding.textImage.setImageBitmap(bitmap)
                        Log.i(TAG, "????????? ?????????: ${bitmap}")

                        deleteImagePath.path = absolutelyPath(photoURI!!) // ?????? ?????? ??????

//                        soundDoodleInit("")

                        /*
                        * ?????? ?????? ??????
                        * */
                        try {
                            exif = ExifInterface(deleteImagePath.path)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        val orientation = exif!!.getAttributeInt(ExifInterface.ORIENTATION_ROTATE_90.toString(), ExifInterface.ORIENTATION_UNDEFINED)
                        val bmRotated: Bitmap = rotateBitmap(bitmap!!, orientation)!!

                        photoURI = getBitmapToUri(bmRotated) // bitmap?????? uri??? ??????
                        deleteFile = File(deleteImagePath.path) // 1. ?????? ????????? ????????? ????????? ?????? ??????
                        deleteFile.delete()
                        deleteImagePath.path = absolutelyPath(photoURI!!)
//                        val resizedBmp = Bitmap.createScaledBitmap(bitmap!!, bitmap.width / 5, bitmap.height/5, true)
//                        photoURI = getImageUri(resizedBmp)
                        // ?????? ????????????
                        var resizeBitmap = resize(this, photoURI!!, 500)
                        photoURI = getBitmapToUri(resizeBitmap!!)
                        deleteFile = File(deleteImagePath.path) // 2. ?????? ????????? ????????? ?????? ??????
                        deleteFile.delete()
                        deleteImagePath.path = absolutelyPath(photoURI!!)

//                        val file = File(deleteImagePath.path)
//                        var fileName = file.getName()
//                        fileName = "${UserInfo.email}.png"
//
//                        Log.i(TAG,"photoURI: "+photoURI)
//                        Log.i(TAG,"????????? ??????: "+ deleteImagePath.path)
//                        Log.i(TAG,"????????? ?????? ${fileName}")
//
//                        bookViewModel.insertImageMemo(file, fileName)



                        /*
                        * ?????? ??????
                        * */
                        CropImage.activity(photoURI)
                            .start(this)

                        // 3. ??????????????? ???????????? ????????? ?????? ?????? ??? ??????

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
//                            startActivityForResult(intent, CROP_FROM_CAMERA); // CROP_FROM_CAMERA case??? ??????
//                        }
//                            .subscribeOn(Schedulers.newThread())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe {
//                                Log.i(TAG, "?????????.")
//                            }


//                        deleteFile = File(deleteImagePath.path)
//                        deleteFile.delete()

                    }
                }

                /*
                * ?????? ?????? ?????? ???..
                * */
                CROP_FROM_CAMERA -> {
                    if(data?.extras != null) {
                        var extras: Bundle = data?.extras!!
                        Log.i(TAG, "?????? ?????????: ${extras.get("data")}")

                        photoURI = getBitmapToUri(extras.get("data") as Bitmap) // bitmap?????? uri??? ??????
                        deleteFile = File(deleteImagePath.path) // 3. ?????? ?????? ??? ????????? ?????? ??????
                        deleteFile.delete()
                        deleteImagePath.path = absolutelyPath(photoURI!!)

                        val file = File(deleteImagePath.path)
                        var fileName = file.getName()
                        fileName = "${UserInfo.email}.png"

                        Log.i(TAG, "photoURI: " + photoURI)
                        Log.i(TAG, "????????? ??????: " + deleteImagePath.path)
                        Log.i(TAG, "????????? ?????? ${fileName}")

                        bookViewModel.insertImageMemo(file, fileName)
                        photoURI = null // ?????? ??? null ??????
                    } else {
                        Log.i(TAG, "?????? ????????? null")
                    }
                }
            }
        }
    }

    var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() { // ?????? ????????? ?????? ??? ??????
            initView()
        }
        override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
            // ?????? ????????? ??????  ??? ??????
            showToast("?????? ????????? ?????? ????????? ???????????? ????????? ??? ????????????.")
        }
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) { // ????????????(??????????????? 6.0) ?????? ?????? ??????
            TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("?????? ???????????? ???????????? ?????? ????????? ???????????????")
                .setDeniedMessage("????????? ???????????? ??????????????? ???????????????.\n [??????] > [??????] ?????? ???????????? ?????????????????????.")
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
        // ????????? ????????? ??????
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        createImageUri("imagefolder"+ Calendar.getInstance().getTime(), "image/jpeg")?.let { uri ->
            photoURI = uri
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            Log.i(TAG, "????????? ????????? uri ${photoURI}")
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
            image = if (Build.VERSION.SDK_INT > 27) { // Api ????????? ????????? ??????
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
            ) // 1???
            var width = options.outWidth
            var height = options.outHeight
            var samplesize = 1
            while (true) { //2???
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
            ) //3???
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