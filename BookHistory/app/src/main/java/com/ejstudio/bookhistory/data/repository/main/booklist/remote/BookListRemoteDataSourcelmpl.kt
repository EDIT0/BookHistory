package com.ejstudio.bookhistory.data.repository.main.booklist.remote

import com.ejstudio.bookhistory.data.api.ApiInterface
import com.ejstudio.bookhistory.data.model.ImageMemoEntity
import com.ejstudio.bookhistory.data.model.TextMemoEntity
import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import com.ejstudio.bookhistory.domain.model.UpdateBookReadingStateModel
import com.ejstudio.bookhistory.util.ImageSenderModule
import io.reactivex.rxjava3.core.Single
import java.io.File

class BookListRemoteDataSourcelmpl(
    private val apiInterface: ApiInterface
) : BookListRemoteDataSource {
    override fun deleteIdxBookInfo(email: String, idx: Int): Single<CheckTrueOrFalseModel> {
        return apiInterface.deleteIdxBookInfo(email, idx)
    }

    override fun updateBookReadingState(email: String, idx: Int, reading_state: String): Single<UpdateBookReadingStateModel> {
        return apiInterface.updateBookReadingState(email, idx, reading_state)
    }

    override fun insertTextMemo(bookIdx: Int, memoContents: String): Single<TextMemoEntity> {
        return apiInterface.insertTextMemo(bookIdx, memoContents)
    }

    override fun deleteIdxTextMemo(textMemoIdx: Int): Single<CheckTrueOrFalseModel> {
        return apiInterface.deleteIdxTextMemo(textMemoIdx)
    }

    override fun updateIdxTextMemo(textMemoIdx: Int, edit_memo_contents: String): Single<CheckTrueOrFalseModel> {
        return apiInterface.updateIdxTextMemo(textMemoIdx, edit_memo_contents)
    }

    override fun insertImageMemo(bookIdx: Int, file: File, fileName: String): Single<ImageMemoEntity> {
        return apiInterface.insertImageMemo(bookIdx, fileName)
    }

    override fun deleteIdxImageMemo(imageMemoIdx: Int): Single<CheckTrueOrFalseModel> {
        return apiInterface.deleteIdxImageMemo(imageMemoIdx)
    }
}