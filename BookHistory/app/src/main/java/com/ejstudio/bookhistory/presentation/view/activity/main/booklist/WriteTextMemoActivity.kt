package com.ejstudio.bookhistory.presentation.view.activity.main.booklist

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.*
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
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityBookBinding
import com.ejstudio.bookhistory.databinding.ActivityWriteTextMemoBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookInfoBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.fragment.main.booklist.BookMenuChangeBottomSheetDialogFragment
import com.ejstudio.bookhistory.presentation.view.viewmodel.login.FindPasswordViewModel
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
    lateinit var dialog: Dialog

    var photoURI: Uri? = null // ????????? ??????????????? Uri??? ????????? ??????
    val FLAG_REQ_CAMERA = 200

    var exif: ExifInterface? = null

    lateinit var tess : TessBaseAPI //Tesseract API ?????? ??????
    var dataPath : String = "" //????????? ?????? ?????? ??????

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.writeTextMemoViewModel = writeTextMemoViewModel

        recvIntent()
        scrollingSetting()
        viewModelCallback()
        buttonClickListener()

//        dataPath = filesDir.toString()+ "/tesseract/" //?????????????????? ?????? ?????? ??????
//
//        checkFile(File(dataPath+"tessdata/"),"kor") //????????? ??????????????? ?????? ??????
//        checkFile(File(dataPath+"tessdata/"),"eng")
//
//        var lang : String = "kor"
//        tess = TessBaseAPI() //api??????
//        tess.init(dataPath,lang) //?????? ????????? ?????????????????? ?????????

    }

    fun recvIntent() {
        writeTextMemoViewModel.book_idx = intent.getIntExtra("book_idx", 0)
        writeTextMemoViewModel.bookTitle = intent.getStringExtra("bookTitle")!!
        Log.i(TAG, "??? idx: " + writeTextMemoViewModel.book_idx)
    }

    fun scrollingSetting() {
        binding.etContents.setOnScrollChangeListener { view, i, i2, i3, i4 ->
            binding.actionBarView.isSelected = binding.etContents.canScrollVertically(-1)
        }
    }

    fun viewModelCallback() {
        with(writeTextMemoViewModel) {
            backButton.observe(this@WriteTextMemoActivity, Observer {
                if(completed_save_memo) {
                    activityBackButton()
                } else {
                    dialog = Dialog(binding.root.context);       // Dialog ?????????
//                deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // ????????? ??????
                    dialog.setContentView(R.layout.dialog_delete_idx_book_info);
                    dialog.findViewById<TextView>(R.id.dialog_tv_subTitle).setText("??????????????? ??????????????????????")
                    dialog.findViewById<TextView>(R.id.dialog_tv_title).setText("???????????? ?????? ????????? ????????????.")
                    showDialog()
                }
//                activityBackButton()
            })
            showToast.observe(this@WriteTextMemoActivity, Observer {
                showToast(toastMessage)
            })
            requestSnackbar.observe(this@WriteTextMemoActivity, Observer {
                when(snackbarMessage) {
                    WriteTextMemoViewModel.MessageSet.NETWORK_NOT_CONNECTED.toString() -> {
                        snackbarMessage = getString(R.string.NETWORK_NOT_CONNECTED)
                    }
                }
                showSnackbar(snackbarMessage)
            })
//            clickOCR.observe(this@WriteTextMemoActivity, Observer {
//                goToCamera()
//            })
        }
    }

    fun buttonClickListener() {
        binding.ibTextCopy.setOnClickListener {
            val clipboardManager: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("TEXT", binding.etContents.getText().toString().trim()) //??????????????? ID?????? ???????????? id ?????? ???????????? ??????
            clipboardManager.setPrimaryClip(clipData)

            showToast("???")
        }
    }

    fun showDialog() {
        dialog.show()

        val dialog_cancel: Button = dialog.findViewById(R.id.dialog_cancel)
        dialog_cancel.setOnClickListener {
            dialog.dismiss()
        }

        val dialog_confirmation: Button = dialog.findViewById(R.id.dialog_confirmation)
        dialog_confirmation.setOnClickListener {
            activityBackButton()
            dialog.dismiss()
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
//        override fun onPermissionGranted() { // ?????? ????????? ?????? ??? ??????
//            initView()
//        }
//        override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
//            // ?????? ????????? ??????  ??? ??????
//            showToast("?????? ????????? ?????? ????????? ???????????? ????????? ??? ????????????.")
//        }
//    }
//
//    private fun checkPermissions() {
//        if (Build.VERSION.SDK_INT >= 23) { // ????????????(??????????????? 6.0) ?????? ?????? ??????
//            TedPermission.with(this)
//                .setPermissionListener(permissionlistener)
//                .setRationaleMessage("?????? ???????????? ???????????? ?????? ????????? ???????????????")
//                .setDeniedMessage("????????? ???????????? ??????????????? ???????????????.\n [??????] > [??????] ?????? ???????????? ?????????????????????.")
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
//        // ????????? ????????? ??????
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
//            image = if (Build.VERSION.SDK_INT > 27) { // Api ????????? ????????? ??????
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
//                        Log.i(TAG, "????????? URI: ${photoURI}")
//                        val bitmap = loadBitmapFromMediaStoreBy(photoURI!!)
////                        binding.textImage.setImageBitmap(bitmap)
//                        Log.i(TAG, "????????? ?????????: ${bitmap}")
//
//                        var path = absolutelyPath(photoURI!!) // ?????? ?????? ??????
//
//
//                        /*
//                        * ?????? ?????? ??????
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
//                        photoURI = getBitmapToUri(bmRotated) // bitmap?????? uri??? ??????
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
//                        Log.i(TAG, "????????? ??????: " + path)
//                        Log.i(TAG, "????????? ?????? ${fileName}")
//                        Log.i(TAG, "?????????: ${bm}")
//
////                        tess.setImage(bm)
////                        Log.i(TAG, "?????? " + tess.utF8Text)
//
//                        processImage(bm) //????????? ????????? ??????????????? ?????????
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
//        //????????? ???????????? ?????? ??? ????????? ??????
//        if(!dir.exists()&&dir.mkdirs()){
//            copyFile(lang)
//        }
//
//        if(dir.exists()){
//            var datafilePath : String = dataPath+"/tessdata/"+lang+".traineddata"
//            var dataFile : File = File(datafilePath)
//            Log.i(TAG, "?????? ??????????????????.")
//            if(!dataFile.exists()){
//                copyFile(lang)
//            }
//        }
//
//    }
//
//    fun copyFile(lang : String){
//        try{
//            //???????????????????????? ??????
//            var filePath : String = dataPath+"/tessdata/"+lang+".traineddata"
//
//            //AssetManager??? ???????????? ?????? ?????? ??????
//            var assetManager : AssetManager = getAssets();
//
//            //byte ???????????? ?????? ??????????????? ??????
//            var inputStream : InputStream = assetManager.open("tessdata/"+lang+".traineddata")
//            var outStream : OutputStream = FileOutputStream(filePath)
//
//
//            //?????? ????????? ?????? ??????????????? ?????? ??????????????? ????????? ????????????.
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
//            Log.v("????????????",e.toString())
//        }catch (e : IOException)
//        {
//            Log.v("????????????",e.toString())
//        }
//    }
//
//    fun processImage(bitmap : Bitmap){
//        showToast("?????? ????????? ????????? ${bitmap}")
//        var ocrResult : String? = null;
//        tess.setImage(bitmap)
//        ocrResult = tess.utF8Text.toString()
//        Log.i(TAG, "?????? " + tess.utF8Text)
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
//            ) // 1???
//            var width = options.outWidth
//            var height = options.outHeight
//            var samplesize = 1
//            while (true) { //2???
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
//            ) //3???
//            resizeBitmap = bitmap
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        }
//        return resizeBitmap
//    }
}