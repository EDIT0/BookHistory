package com.ejstudio.bookhistory.data.repository.main.booklist.local

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.ImageMemoEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface BookListLocalDataSource {
    fun getTotalBookList(email: String) : LiveData<List<BookListEntity>>
    fun getBeforeReadBookList(email: String, reading_state: String) : LiveData<List<BookListEntity>>
    fun getReadingBookList(email: String, reading_state: String) : LiveData<List<BookListEntity>>
    fun getEndReadBookList(email: String, reading_state: String) : LiveData<List<BookListEntity>>
    fun getIdxBookInfo(email: String, idx: Int, reading_state: String) : LiveData<BookListEntity>
    fun deleteIdxBookInfo(email: String, idx: Int) : Completable
    fun updateBookReadingState_before(email: String, idx: Int, reading_state: String, reading_start_datetime: String, reading_end_datetime: String, add_datetiem: String) : Completable
    fun updateBookReadingState_reading(email: String, idx: Int, reading_state: String, reading_start_datetime: String) : Completable
    fun updateBookReadingState_end(email: String, idx: Int, reading_state: String, reading_end_datetime: String) : Completable
    fun getTextMemo(bookListIdx: Int) : LiveData<List<TextMemoEntity>>
    fun insertTextMemo(idx: Int, bookIdx: Int, memoContents: String, save_datetime: String)
    fun getIdxTextMemo(textMemoIdx: Int) : LiveData<TextMemoEntity>
    fun deleteIdxTextMemo(textMemoIdx: Int) : Completable
    fun updateIdxTextMemo(textMemoIdx: Int, edit_memo_contents: String) : Completable
    fun insertImageMemo(idx: Int, bookIdx: Int, memoImagePath: String, save_datetime: String) : Completable
    fun getImageMemo(bookListIdx: Int) : LiveData<List<ImageMemoEntity>>
    fun deleteIdxImageMemo(imageMemoIdx: Int) : Completable
}