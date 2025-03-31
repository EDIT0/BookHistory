package com.ejstudio.bookhistory.data.repository.main.booklist

import android.util.Log
import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.ImageMemoEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.data.repository.main.booklist.local.BookListLocalDataSource
import com.ejstudio.bookhistory.data.repository.main.booklist.remote.BookListRemoteDataSource
import com.ejstudio.bookhistory.domain.repository.BookListRepository
import com.ejstudio.bookhistory.presentation.view.activity.main.booklist.BookActivity
import com.ejstudio.bookhistory.util.Converter
import com.ejstudio.bookhistory.util.ImageSenderModule
import com.ejstudio.bookhistory.util.UserInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.Exception

class BookListRepositorylmpl(
    private val bookListLocalDataSource: BookListLocalDataSource,
    private val bookListRemoteDataSource: BookListRemoteDataSource
) : BookListRepository {

    private val TAG: String? = BookListRepositorylmpl::class.java.simpleName

    override fun getTotalBookList(email: String): LiveData<List<BookListEntity>> {
        return bookListLocalDataSource.getTotalBookList(email)
    }

    override fun getBeforeReadBookList(email: String, reading_state: String): LiveData<List<BookListEntity>> {
        return bookListLocalDataSource.getBeforeReadBookList(email, reading_state)
    }

    override fun getReadingBookList(email: String, reading_state: String): LiveData<List<BookListEntity>> {
        return bookListLocalDataSource.getReadingBookList(email, reading_state)
    }

    override fun getEndReadBookList(email: String, reading_state: String, pickerYear: String): LiveData<List<BookListEntity>> {
        return bookListLocalDataSource.getEndReadBookList(email, reading_state, pickerYear)
    }

    override fun getIdxBookInfo(email: String, idx: Int, reading_state: String): LiveData<BookListEntity> {
        return bookListLocalDataSource.getIdxBookInfo(email, idx, reading_state)
    }

    override fun deleteIdxBookInfo(email: String, idx: Int): Single<Boolean> {
        return bookListRemoteDataSource.deleteIdxBookInfo(email, idx)
            .flatMap { returnValue ->
                if(returnValue.returnvalue.toBoolean()) {
                    bookListLocalDataSource.deleteIdxBookInfo(email, idx)
                        .subscribe({
                            Log.i(TAG, "삭제 성공")
                        })
                    Single.just(true)
                } else {
                    Log.i(TAG, "삭제 실패")
                    Single.just(false)
                }
            }
    }

    override fun updateBookReadingState(email: String, idx: Int, reading_state: String): Single<Boolean> {
        return bookListRemoteDataSource.updateBookReadingState(email, idx, reading_state)
            .flatMap { returnValue ->
                if(returnValue.returnvalue.toBoolean()) {
                    Log.i(TAG, "1차 업데이트 성공 : " + returnValue.returnvalue + " / " + returnValue.reading_start_datetime + " / " + returnValue.reading_end_datetime)
                    if(returnValue.add_datetime != null) {
                        bookListLocalDataSource.updateBookReadingState_before(email, idx, reading_state, "", "", returnValue.add_datetime)
                            .subscribe({
                                Log.i(TAG, "업데이트(before) 성공")
                            })
                    }
                    else if(returnValue.reading_start_datetime == null) {
                        bookListLocalDataSource.updateBookReadingState_end(email, idx, reading_state, returnValue.reading_end_datetime)
                            .subscribe({
                                Log.i(TAG, "업데이트(end) 성공")
                            })
                    } else if(returnValue.reading_end_datetime == null) {
                        bookListLocalDataSource.updateBookReadingState_reading(email, idx, reading_state, returnValue.reading_start_datetime)
                            .subscribe({
                                Log.i(TAG, "업데이트(reading) 성공")
                            })
                    }
                    Single.just(true)
                } else {
                    Single.just(false)
                }
            }
    }

    override fun getTextMemo(bookListIdx: Int): LiveData<List<TextMemoEntity>> {
        return bookListLocalDataSource.getTextMemo(bookListIdx)
    }

    override fun insertTextMemo(bookIdx: Int, memoContents: String): Single<Boolean> {
        return bookListRemoteDataSource.insertTextMemo(bookIdx, memoContents)
            .flatMap {
                // 메모 내용 복호화
                Log.i(TAG, "로컬 데이터베이스에 들어갈 메모 데이터: ${it.idx!!} ${it.booklist_idx!!} ${Converter.decByKey(Converter.key, it.memo_contents).toString()} ${it.save_datetime!!}")
                bookListLocalDataSource.insertTextMemo(it.idx!!, it.booklist_idx, Converter.decByKey(Converter.key, it.memo_contents).toString(), it.save_datetime)
                Single.just(true)
            }
    }

    override fun getIdxTextMemo(textMemoIdx: Int): LiveData<TextMemoEntity> {
        return bookListLocalDataSource.getIdxTextMemo(textMemoIdx)
    }

    override fun deleteIdxTextMemo(textMemoIdx: Int): Single<Boolean> {
        return bookListRemoteDataSource.deleteIdxTextMemo(textMemoIdx)
            .flatMap {
                if(it.returnvalue.toBoolean()) {
                    Log.i(TAG, "서버 메모 삭제 성공")
                    bookListLocalDataSource.deleteIdxTextMemo(textMemoIdx)
                        .subscribe {
                            Log.i(TAG, "로컬 메모 삭제 성공")
                        }
                    Single.just(true)
                } else {
                    Single.just(false)
                }
            }
    }

    override fun updateIdxTextMemo(textMemoIdx: Int, edit_memo_contents: String): Single<Boolean> {
        return bookListRemoteDataSource.updateIdxTextMemo(textMemoIdx, edit_memo_contents)
            .flatMap {
                if(it.returnvalue.toBoolean()) {
                    Log.i(TAG, "서버 메모 업데이트 성공")
                    bookListLocalDataSource.updateIdxTextMemo(textMemoIdx, edit_memo_contents)
                        .subscribe {
                            Log.i(TAG, "로컬 메모 업데이트 성공")
                        }
                    Single.just(true)
                } else {
                    Single.just(false)
                }
            }
    }

    override fun insertImageMemo(bookIdx: Int, file: File, fileName: String): Single<Boolean> {
        return bookListRemoteDataSource.insertImageMemo(bookIdx, file, fileName.trim())
            .flatMap {
//                Log.i(TAG, "로컬 데이터베이스에 들어갈 메모 데이터: ${it.idx} ${it.booklist_idx} ${it.memo_image.trim()} ${it.save_datetime}")
//                bookListLocalDataSource.insertImageMemo(it.idx, it.booklist_idx, it.memo_image.trim(), it.save_datetime)

                val imageSenderModule = ImageSenderModule.getInstance()
                var (server, body) = imageSenderModule.SendImageModule(file, Converter.decByKey(Converter.key, it.memo_image?.trim())!!)
                server.imageSenderToServer(UserInfo.email, body).enqueue(object: Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d(TAG,"에러")
                        // 이미지 이름 복호화
                        Log.i(TAG, "로컬 데이터베이스에 들어갈 메모 데이터: ${it.idx!!} ${it.booklist_idx!!} ${Converter.decByKey(Converter.key, it.memo_image?.trim())} ${it.save_datetime!!}")
                        bookListLocalDataSource.insertImageMemo(it.idx!!, it.booklist_idx!!, Converter.decByKey(Converter.key, it.memo_image?.trim())!!, it.save_datetime!!)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Log.i(TAG, "로컬 데이터베이스에 들어갈 메모 데이터 성공")
                                Log.i(TAG, "파일 삭제: ${BookActivity.deleteImagePath.path}")
                                var deleteFile: File? = File(BookActivity.deleteImagePath.path)
                                deleteFile?.delete()
                                deleteFile = null
                                BookActivity.deleteImagePath.path = ""
                            }, {
                                Log.i(TAG, "로컬 데이터베이스에 들어갈 메모 데이터 에러: " + it.message.toString())
                            })

                    }
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            Log.d(TAG,""+response?.body().toString())
                        } else {
                        }
                    }
                })

                Single.just(true)
            }
    }

    override fun getImageMemo(bookListIdx: Int): LiveData<List<ImageMemoEntity>> {
        return bookListLocalDataSource.getImageMemo(bookListIdx)
    }

    override fun deleteIdxImageMemo(imageMemoIdx: Int): Single<Boolean> {
        Log.i(TAG, "삭제 호출함: " + imageMemoIdx.toString().trim().toInt())
        return bookListRemoteDataSource.deleteIdxImageMemo(imageMemoIdx.toString().trim().toInt())
            .flatMap {
                if(it.returnvalue.toBoolean()) {
                    Log.i(TAG, "서버 이미지 메모 삭제 성공")
                    bookListLocalDataSource.deleteIdxImageMemo(imageMemoIdx)
                        .subscribe {
                            Log.i(TAG, "로컬 이미지 메모 삭제 성공")
                        }
                    Single.just(true)
                } else {
                    Single.just(false)
                }
            }
    }
}