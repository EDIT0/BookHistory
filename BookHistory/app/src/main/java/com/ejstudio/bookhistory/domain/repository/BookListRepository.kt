package com.ejstudio.bookhistory.domain.repository

import androidx.lifecycle.LiveData
import com.ejstudio.bookhistory.data.model.BookListEntity
import com.ejstudio.bookhistory.data.model.ImageMemoEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.io.File

interface BookListRepository {
    fun getTotalBookList(email: String) : LiveData<List<BookListEntity>>
    fun getBeforeReadBookList(email: String, reading_state: String) : LiveData<List<BookListEntity>>
    fun getReadingBookList(email: String, reading_state: String) : LiveData<List<BookListEntity>>
    fun getEndReadBookList(email: String, reading_state: String, pickerYear: String) : LiveData<List<BookListEntity>>
    fun getIdxBookInfo(email: String, idx: Int, reading_state: String) : LiveData<BookListEntity>
    fun deleteIdxBookInfo(email: String, idx: Int) : Single<Boolean>
    fun updateBookReadingState(email: String, idx: Int, reading_state: String) : Single<Boolean>
    fun getTextMemo(bookListIdx: Int) : LiveData<List<TextMemoEntity>>
    fun insertTextMemo(bookIdx: Int, memoContents: String) : Single<Boolean>
    fun getIdxTextMemo(textMemoIdx: Int) : LiveData<TextMemoEntity>
    fun deleteIdxTextMemo(textMemoIdx: Int) : Single<Boolean>
    fun updateIdxTextMemo(textMemoIdx: Int, edit_memo_contents: String) : Single<Boolean>
    fun insertImageMemo(bookIdx: Int, file: File, fileName: String) : Single<Boolean>
    fun getImageMemo(bookListIdx: Int) : LiveData<List<ImageMemoEntity>>
    fun deleteIdxImageMemo(imageMemoIdx: Int) : Single<Boolean>
}