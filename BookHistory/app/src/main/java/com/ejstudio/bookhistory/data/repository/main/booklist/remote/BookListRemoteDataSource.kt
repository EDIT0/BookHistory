package com.ejstudio.bookhistory.data.repository.main.booklist.remote

import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import com.ejstudio.bookhistory.domain.model.UpdateBookReadingStateModel
import io.reactivex.rxjava3.core.Single

interface BookListRemoteDataSource {
    fun deleteIdxBookInfo(email: String, idx: Int) : Single<CheckTrueOrFalseModel>
    fun updateBookReadingState(email: String, idx: Int, reading_state: String) : Single<UpdateBookReadingStateModel>
    fun insertTextMemo(bookIdx: Int, memoContents: String) : Single<TextMemoEntity>
    fun deleteIdxTextMemo(textMemoIdx: Int) : Single<CheckTrueOrFalseModel>
}