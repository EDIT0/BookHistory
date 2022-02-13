package com.ejstudio.bookhistory.presentation.view.activity.main.booklist

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityBookBinding
import com.ejstudio.bookhistory.databinding.ActivityWriteTextMemoBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookInfoBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookMenuChangeBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.BookViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.WriteTextMemoViewModel
import com.ejstudio.bookhistory.util.UserInfo
import com.googlecode.leptonica.android.Pix
import com.googlecode.leptonica.android.ReadFile
import com.googlecode.tesseract.android.TessBaseAPI
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*

class WriteTextMemoActivity : BaseActivity<ActivityWriteTextMemoBinding>(R.layout.activity_write_text_memo) {

    private val TAG: String? = WriteTextMemoActivity::class.java.simpleName
    public val writeTextMemoViewModel: WriteTextMemoViewModel by viewModel()

    var photoURI: Uri? = null // 카메라 원본이미지 Uri를 저장할 변수
    val FLAG_REQ_CAMERA = 200

    var exif: ExifInterface? = null

    lateinit var tess : TessBaseAPI //Tesseract API 객체 생성
    var dataPath : String = "" //데이터 경로 변수 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.writeTextMemoViewModel = writeTextMemoViewModel

        recvIntent()
        viewModelCallback()

//        dataPath = filesDir.toString()+ "/tesseract/" //언어데이터의 경로 미리 지정
//
//        checkFile(File(dataPath+"tessdata/"),"kor") //사용할 언어파일의 이름 지정
//        checkFile(File(dataPath+"tessdata/"),"eng")
//
//        var lang : String = "kor"
//        tess = TessBaseAPI() //api준비
//        tess.init(dataPath,lang) //해당 사용할 언어데이터로 초기화

    }

    fun recvIntent() {
        writeTextMemoViewModel.book_idx = intent.getIntExtra("book_idx", 0)
        writeTextMemoViewModel.bookTitle = intent.getStringExtra("bookTitle")!!
        Log.i(TAG, "책 idx: " + writeTextMemoViewModel.book_idx)
    }

    fun viewModelCallback() {
        with(writeTextMemoViewModel) {
            backButton.observe(this@WriteTextMemoActivity, Observer {
                activityBackButton()
            })
            showToast.observe(this@WriteTextMemoActivity, Observer {
                showToast(toastMessage)
            })
//            clickOCR.observe(this@WriteTextMemoActivity, Observer {
//                goToCamera()
//            })
        }
    }

    fun activityBackButton() {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.not_move_activity, R.anim.rightout_activity)
    }

//    fun goToCamera() {
//        checkPermissions()
//    }
//
//    var permissionlistener: PermissionListener = object : PermissionListener {
//        override fun onPermissionGranted() { // 권한 허가시 실행 할 내용
//            initView()
//        }
//        override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
//            // 권한 거부시 실행  할 내용
//            showToast("권한 허용을 하지 않으면 서비스를 이용할 수 없습니다.")
//        }
//    }
//
//    private fun checkPermissions() {
//        if (Build.VERSION.SDK_INT >= 23) { // 마시멜로(안드로이드 6.0) 이상 권한 체크
//            TedPermission.with(this)
//                .setPermissionListener(permissionlistener)
//                .setRationaleMessage("앱을 이용하기 위해서는 접근 권한이 필요합니다")
//                .setDeniedMessage("앱에서 요구하는 권한설정이 필요합니다.\n [설정] > [권한] 에서 사용으로 활성화해주세요.")
//                .setPermissions(
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.CAMERA,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                ).check()
//        } else {
//            initView()
//        }
//    }
//
//    fun initView() {
//        // 카메라 인텐트 생성
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        createImageUri("imagefolder"+ Calendar.getInstance().getTime(), "image/jpeg")?.let { uri ->
//            photoURI = uri
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//            startActivityForResult(takePictureIntent, FLAG_REQ_CAMERA)
//        }
//    }
//
//    fun createImageUri(filename: String, mimeType: String) : Uri? {
//        var values = ContentValues()
//        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
//        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
//        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
//    }
//
//    fun absolutelyPath(path: Uri): String {
//
//        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
//        var c: Cursor? = contentResolver.query(path, proj, null, null, null)
//        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        c?.moveToFirst()
//
//        var result = c?.getString(index!!)
//
//        return result!!
//    }
//
//    private fun getBitmapToUri(inImage: Bitmap): Uri? {
//        val bytes = ByteArrayOutputStream()
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//        val path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null)
//        return Uri.parse(path)
//    }
//
//    fun loadBitmapFromMediaStoreBy(photoUri: Uri): Bitmap? {
//        var image: Bitmap? = null
//        try {
//            image = if (Build.VERSION.SDK_INT > 27) { // Api 버전별 이미지 처리
//                val source: ImageDecoder.Source = ImageDecoder.createSource(this.contentResolver, photoUri)
//                ImageDecoder.decodeBitmap(source)
//            } else {
//                MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return image
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            when (requestCode) {
//                FLAG_REQ_CAMERA -> {
//                    if (photoURI != null) {
//                        Log.i(TAG, "이미지 URI: ${photoURI}")
//                        val bitmap = loadBitmapFromMediaStoreBy(photoURI!!)
////                        binding.textImage.setImageBitmap(bitmap)
//                        Log.i(TAG, "이미지 비트맵: ${bitmap}")
//
//                        var path = absolutelyPath(photoURI!!) // 파일 경로 얻기
//
//
//                        /*
//                        * 사진 각도 맞춤
//                        * */
//                        try {
//                            exif = ExifInterface(path)
//                        } catch (e: IOException) {
//                            e.printStackTrace()
//                        }
//                        val orientation = exif!!.getAttributeInt(
//                            ExifInterface.ORIENTATION_ROTATE_90.toString(),
//                            ExifInterface.ORIENTATION_UNDEFINED
//                        )
//                        val bmRotated: Bitmap = rotateBitmap(bitmap!!, orientation)!!
//
//                        photoURI = getBitmapToUri(bmRotated) // bitmap에서 uri로 변경
//
//                        var resizeBitmap = resize(this, photoURI!!, 2000)
//                        photoURI = getBitmapToUri(resizeBitmap!!)
//
//
//                        path = absolutelyPath(photoURI!!)
//
//                        val file = File(path)
//                        var fileName = file.getName()
//                        fileName = "${UserInfo.email}.png"
//
//                        val bm = BitmapFactory.decodeFile(path)
//
//                        Log.i(TAG, "photoURI: " + photoURI)
//                        Log.i(TAG, "이미지 경로: " + path)
//                        Log.i(TAG, "이미지 제목 ${fileName}")
//                        Log.i(TAG, "비트맵: ${bm}")
//
////                        tess.setImage(bm)
////                        Log.i(TAG, "출력 " + tess.utF8Text)
//
//                        processImage(bm) //이미지 가공후 텍스트뷰에 띄우기
////                        bookViewModel.insertImageMemo(file, fileName)
//
//
////                        val cropPictureIntent = Intent("com.android.camera.action.CROP")
////                        cropPictureIntent.setDataAndType(photoURI, "image/*")
////                        startActivityForResult(cropPictureIntent, CROP_FROM_CAMERA)
//
//                    }
//                }
//            }
//        }
//    }
//
//    fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap? {
//        val matrix = Matrix()
//        when (orientation) {
//            ExifInterface.ORIENTATION_NORMAL -> return bitmap
//            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
//            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
//            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
//                matrix.setRotate(180f)
//                matrix.postScale(-1f, 1f)
//            }
//            ExifInterface.ORIENTATION_TRANSPOSE -> {
//                matrix.setRotate(90f)
//                matrix.postScale(-1f, 1f)
//            }
//            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
//            ExifInterface.ORIENTATION_TRANSVERSE -> {
//                matrix.setRotate(-90f)
//                matrix.postScale(-1f, 1f)
//            }
//            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
//            else -> return bitmap
//        }
//        return try {
//            val bmRotated =
//                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
//            bitmap.recycle()
//            bmRotated
//        } catch (e: OutOfMemoryError) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//
//
//
//    fun checkFile(dir : File, lang : String){
//
//        //파일의 존재여부 확인 후 내부로 복사
//        if(!dir.exists()&&dir.mkdirs()){
//            copyFile(lang)
//        }
//
//        if(dir.exists()){
//            var datafilePath : String = dataPath+"/tessdata/"+lang+".traineddata"
//            var dataFile : File = File(datafilePath)
//            Log.i(TAG, "언어 파일있습니다.")
//            if(!dataFile.exists()){
//                copyFile(lang)
//            }
//        }
//
//    }
//
//    fun copyFile(lang : String){
//        try{
//            //언어데이타파일의 위치
//            var filePath : String = dataPath+"/tessdata/"+lang+".traineddata"
//
//            //AssetManager를 사용하기 위한 객체 생성
//            var assetManager : AssetManager = getAssets();
//
//            //byte 스트림을 읽기 쓰기용으로 열기
//            var inputStream : InputStream = assetManager.open("tessdata/"+lang+".traineddata")
//            var outStream : OutputStream = FileOutputStream(filePath)
//
//
//            //위에 적어둔 파일 경로쪽으로 해당 바이트코드 파일을 복사한다.
//            var buffer : ByteArray = ByteArray(1024)
//
//            var read : Int = 0
//            read = inputStream.read(buffer)
//            while(read!=-1){
//                outStream.write(buffer,0,read)
//                read = inputStream.read(buffer)
//            }
//            outStream.flush()
//            outStream.close()
//            inputStream.close()
//
//        }catch(e : FileNotFoundException){
//            Log.v("오류발생",e.toString())
//        }catch (e : IOException)
//        {
//            Log.v("오류발생",e.toString())
//        }
//    }
//
//    fun processImage(bitmap : Bitmap){
//        showToast("잠시 기다려 주세요 ${bitmap}")
//        var ocrResult : String? = null;
//        tess.setImage(bitmap)
//        ocrResult = tess.utF8Text.toString()
//        Log.i(TAG, "출력 " + tess.utF8Text)
//        Log.i(TAG, ocrResult.toString())
//        var strBuilder = StringBuilder()
//        for(i in 0 until writeTextMemoViewModel.memo_contents.length) {
//            strBuilder.append(writeTextMemoViewModel.memo_contents.get(i))
//        }
//        strBuilder.append(ocrResult)
//        writeTextMemoViewModel.memo_contents = strBuilder.toString()
//    }
//
//    private fun resize(context: Context, uri: Uri, resize: Int): Bitmap? {
//        var resizeBitmap: Bitmap? = null
//        val options = BitmapFactory.Options()
//        try {
//            BitmapFactory.decodeStream(
//                context.getContentResolver().openInputStream(uri),
//                null,
//                options
//            ) // 1번
//            var width = options.outWidth
//            var height = options.outHeight
//            var samplesize = 1
//            while (true) { //2번
//                if (width / 2 < resize || height / 2 < resize) break
//                width /= 2
//                height /= 2
//                samplesize *= 2
//            }
//            options.inSampleSize = samplesize
//            val bitmap = BitmapFactory.decodeStream(
//                context.getContentResolver().openInputStream(uri),
//                null,
//                options
//            ) //3번
//            resizeBitmap = bitmap
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        }
//        return resizeBitmap
//    }
}