package com.ejstudio.bookhistory.data.repository.main.booklist.local

import android.util.Log
import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.db.BookListDao
import com.ejstudio.bookhistory.data.db.ImageMemoDao
import com.ejstudio.bookhistory.data.db.TextMemoDao
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.ImageMemoEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import io.reactivex.rxjava3.core.Completable

class BookListLocalDataSourcelmpl(
    private val bookListDao: BookListDao,
    private val textMemoDao: TextMemoDao,
    private val imageMemoDao: ImageMemoDao
) : BookListLocalDataSource {


    private val TAG = BookListLocalDataSourcelmpl::class.java.simpleName

    override fun getTotalBookList(email: String): LiveData<List<BookListEntity>> {
        return bookListDao.getAllBookList(email)
    }

    override fun getBeforeReadBookList(email: String, reading_state: String): LiveData<List<BookListEntity>> {
        Log.i(TAG, "getBeforeReadBookList() 호출")
        return bookListDao.getBookList_before(email, reading_state)

    }
    override fun getReadingBookList(email: String, reading_state: String): LiveData<List<BookListEntity>> {
        Log.i(TAG, "getReadingBookList() 호출")
        return bookListDao.getBookList_reading(email, reading_state)
    }

    override fun getEndReadBookList(email: String, reading_state: String): LiveData<List<BookListEntity>> {
        Log.i(TAG, "getEndReadBookList() 호출")
        return bookListDao.getBookList_end(email, reading_state)
    }

    override fun getIdxBookInfo(email: String, idx: Int, reading_state: String): LiveData<BookListEntity> {
        return bookListDao.getIdxBookInfo(email, idx, reading_state)
    }

    override fun deleteIdxBookInfo(email: String, idx: Int): Completable {
        return bookListDao.deleteIdxBookInfo(email, idx)
    }

    override fun updateBookReadingState_before(email: String, idx: Int, reading_state: String, reading_start_datetime: String, reading_end_datetime: String, add_datetime: String): Completable {
        return bookListDao.updateBookReadingState_before(email, idx, reading_state, reading_start_datetime, reading_end_datetime, add_datetime)
    }

    override fun updateBookReadingState_reading(email: String, idx: Int, reading_state: String, reading_start_datetime: String): Completable {
        return bookListDao.updateBookReadingState_reading(email, idx, reading_state, reading_start_datetime)
    }

    override fun updateBookReadingState_end(email: String, idx: Int, reading_state: String, reading_end_datetime: String): Completable {
        return bookListDao.updateBookReadingState_end(email, idx, reading_state, reading_end_datetime)
    }


//    override fun updateBookReadingState(email: String, idx: Int, reading_state: String): Completable {
//        return bookListDao.updateBookReadingState(email, idx, reading_state)
//    }

    override fun getTextMemo(bookListIdx: Int): LiveData<List<TextMemoEntity>> {
        return textMemoDao.getTotalTextMemo(bookListIdx)
    }

    override fun insertTextMemo(idx: Int, bookIdx: Int, memoContents: String, save_datetime: String) {
        return textMemoDao.insertTextMemo(TextMemoEntity(idx, bookIdx, memoContents, save_datetime))
    }

    override fun getIdxTextMemo(textMemoIdx: Int): LiveData<TextMemoEntity> {
        return textMemoDao.getIdxTextMemo(textMemoIdx)
    }

    override fun deleteIdxTextMemo(textMemoIdx: Int): Completable {
        return textMemoDao.deleteIdxTextMemo(textMemoIdx)
    }

    override fun updateIdxTextMemo(textMemoIdx: Int, edit_memo_contents: String): Completable {
        return textMemoDao.updateIdxTextMemo(textMemoIdx, edit_memo_contents)
    }

    override fun insertImageMemo(idx: Int, bookIdx: Int, memoImagePath: String, save_datetime: String) : Completable {
        return imageMemoDao.insertImageMemo(ImageMemoEntity(idx, bookIdx, memoImagePath, save_datetime))
    }

    override fun getImageMemo(bookListIdx: Int): LiveData<List<ImageMemoEntity>> {
        return imageMemoDao.getTotalImageMemo(bookListIdx)
    }

    override fun deleteIdxImageMemo(imageMemoIdx: Int): Completable {
        return imageMemoDao.deleteIdxImageMemo(imageMemoIdx)
    }
}