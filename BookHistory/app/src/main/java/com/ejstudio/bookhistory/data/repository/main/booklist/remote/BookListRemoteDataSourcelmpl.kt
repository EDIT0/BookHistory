package com.ejstudio.bookhistory.data.repository.main.booklist.remote

import android.util.Log
import com.ejstudio.bookhistory.data.api.ApiInterface
import com.ejstudio.bookhistory.data.model.ImageMemoEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import com.ejstudio.bookhistory.domain.model.UpdateBookReadingStateModel
import com.ejstudio.bookhistory.util.Converter
import com.ejstudio.bookhistory.util.ImageSenderModule
import com.ejstudio.bookhistory.util.UserInfo
import io.reactivex.rxjava3.core.Single
import java.io.File

class BookListRemoteDataSourcelmpl(
    private val apiInterface: ApiInterface
) : BookListRemoteDataSource {

    private val TAG: String? = BookListRemoteDataSourcelmpl::class.java.simpleName

    var encText: String = ""
    var encTextContents: String = ""
    var ecnImageFileName: String = ""

    override fun deleteIdxBookInfo(email: String, idx: Int): Single<CheckTrueOrFalseModel> {
        try {
            encText = Converter.encByKey(Converter.key, email)!!
//            decText = Converter.decByKey(Converter.key, encText)!!
            Log.i(TAG, "이메일 암호화 결과 : $encText")
//            Log.i(TAG, "이메일 복호화 결과 : $decText")
        } catch (e: Exception) {
            Log.i(TAG, "이메일 암호화 에러: " + e.printStackTrace())
        }
        return apiInterface.deleteIdxBookInfo(encText, idx)
    }

    override fun updateBookReadingState(email: String, idx: Int, reading_state: String): Single<UpdateBookReadingStateModel> {
        try {
            encText = Converter.encByKey(Converter.key, email)!!
//            decText = Converter.decByKey(Converter.key, encText)!!
            Log.i(TAG, "이메일 암호화 결과 : $encText")
//            Log.i(TAG, "이메일 복호화 결과 : $decText")
        } catch (e: Exception) {
            Log.i(TAG, "이메일 암호화 에러: " + e.printStackTrace())
        }
        return apiInterface.updateBookReadingState(encText, idx, reading_state)
    }

    override fun insertTextMemo(bookIdx: Int, memoContents: String): Single<TextMemoEntity> {
        try {
            encText = Converter.encByKey(Converter.key, UserInfo.email)!!
            encTextContents = Converter.encByKey(Converter.key, memoContents)!!
//            decText = Converter.decByKey(Converter.key, encText)!!
            Log.i(TAG, "이메일 암호화 결과 : $encText")
            Log.i(TAG, "메모내용 암호화 결과 : ${encTextContents}")
//            Log.i(TAG, "이메일 복호화 결과 : $decText")
        } catch (e: Exception) {
            Log.i(TAG, "이메일, 메모내용 암호화 에러: " + e.printStackTrace())
        }
        return apiInterface.insertTextMemo(bookIdx, encTextContents, encText)
    }

    override fun deleteIdxTextMemo(textMemoIdx: Int): Single<CheckTrueOrFalseModel> {
        return apiInterface.deleteIdxTextMemo(textMemoIdx)
    }

    override fun updateIdxTextMemo(textMemoIdx: Int, edit_memo_contents: String): Single<CheckTrueOrFalseModel> {
        try {
            encTextContents = Converter.encByKey(Converter.key, edit_memo_contents)!!
//            decText = Converter.decByKey(Converter.key, encText)!!
            Log.i(TAG, "편집메모내용 암호화 결과 : ${encTextContents}")
//            Log.i(TAG, "이메일 복호화 결과 : $decText")
        } catch (e: Exception) {
            Log.i(TAG, "편집메모내용 암호화 에러: " + e.printStackTrace())
        }
        return apiInterface.updateIdxTextMemo(textMemoIdx, encTextContents)
    }

    override fun insertImageMemo(bookIdx: Int, file: File, fileName: String): Single<ImageMemoEntity> {
        try {
            encText = Converter.encByKey(Converter.key, UserInfo.email)!!
            ecnImageFileName = Converter.encByKey(Converter.key, fileName)!!
//            decText = Converter.decByKey(Converter.key, encText)!!
            Log.i(TAG, "이메일 암호화 결과 : $encText")
            Log.i(TAG, "이미지이름 암호화 결과 : ${ecnImageFileName}")
//            Log.i(TAG, "이메일 복호화 결과 : $decText")
        } catch (e: Exception) {
            Log.i(TAG, "이메일, 이미지이름 암호화 에러: " + e.printStackTrace())
        }
        return apiInterface.insertImageMemo(bookIdx, ecnImageFileName, encText)
    }

    override fun deleteIdxImageMemo(imageMemoIdx: Int): Single<CheckTrueOrFalseModel> {
        return apiInterface.deleteIdxImageMemo(imageMemoIdx)
    }
}