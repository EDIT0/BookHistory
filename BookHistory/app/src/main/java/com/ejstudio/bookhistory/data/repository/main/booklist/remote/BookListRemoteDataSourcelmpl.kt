package com.ejstudio.bookhistory.data.repository.main.booklist.remote

import com.ejstudio.bookhistory.data.api.ApiInterface
import com.ejstudio.bookhistory.domain.model.CheckTrueOrFalseModel
import io.reactivex.rxjava3.core.Single

class BookListRemoteDataSourcelmpl(
    private val apiInterface: ApiInterface
) : BookListRemoteDataSource {
    override fun deleteIdxBookInfo(email: String, idx: Int): Single<CheckTrueOrFalseModel> {
        return apiInterface.deleteIdxBookInfo(email, idx)
    }

    override fun updateBookReadingState(email: String, idx: Int, reading_state: String): Single<CheckTrueOrFalseModel> {
        return apiInterface.updateBookReadingState(email, idx, reading_state)
    }
}